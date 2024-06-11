import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.Type;

public class DevirtualizePrototypeMethodsTest {

 private static final String JS_SOURCE =
     "var a = {\n" +
     "  prototype: {\n" +
     "    accumulate: function(value) {\n" +
     "      this.total += value; return this.total\n" +
     "    }\n" +
     "  }\n" +
     "};\n" +
     "var total = a.accumulate(2);";

 @Test
 public void testDevirtualizePrototypeMethods() throws Exception {
   Compiler compiler = new Compiler();
   compiler.init();
   Node root = Node.parse(JS_SOURCE);
   SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
   defFinder.process(null, root);

   CompilerPass devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
   devirtualizePrototypeMethods.process(null, root);

   Node expectedRoot = Node.parse(
       "var accumulate = function(self, value) {\n" +
       "  self.total += value; return self.total\n" +
       "};\n" +
       "var total = accumulate(a, 2);");

   assertEquals(expectedRoot.toString(), root.toString());
 }
}