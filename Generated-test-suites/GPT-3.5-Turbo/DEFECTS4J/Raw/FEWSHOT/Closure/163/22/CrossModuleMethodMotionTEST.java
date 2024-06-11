import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

public class CrossModuleMethodMotionTest {
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new Compiler();
    IdGenerator idGenerator = new IdGenerator();
    boolean canModifyExterns = true;
    CrossModuleMethodMotion methodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    Node externRoot = new Node();
    Node root = new Node();
    methodMotion.process(externRoot, root);
  }
  
  @Test
  public void testMoveMethods() {
    Collection<AnalyzePrototypeProperties.NameInfo> allNameInfo = new Collection<AnalyzePrototypeProperties.NameInfo>();
    AbstractCompiler compiler = new Compiler();
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion methodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);
    methodMotion.moveMethods(allNameInfo);
  }
  
  @Test
  public void testConstructor() {
    AbstractCompiler compiler = new Compiler();
    IdGenerator idGenerator = new IdGenerator();
    boolean canModifyExterns = true;
    CrossModuleMethodMotion methodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    assertEquals(compiler, methodMotion.compiler);
    assertEquals(idGenerator, methodMotion.idGenerator);
    assertEquals(true, methodMotion.canModifyExterns);
  }
  
  @Test
  public void testHasGeneratedAnyIds() {
    IdGenerator idGenerator = new IdGenerator();
    assertEquals(false, idGenerator.hasGeneratedAnyIds());
    idGenerator.newId();
    assertEquals(true, idGenerator.hasGeneratedAnyIds());
  }
  
  @Test
  public void testNewId() {
    IdGenerator idGenerator = new IdGenerator();
    assertEquals(0, idGenerator.newId());
  }
  
  @Test
  public void testGeneratedCode() {
    CrossModuleMethodMotion methodMotion = new CrossModuleMethodMotion(new Compiler(), new IdGenerator(), true);
    assertEquals(
      "var JSCompiler_stubMap = [];" +
      "function JSCompiler_stubMethod(JSCompiler_stubMethod_id) {" +
      "  return function() {" +
      "    return JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(" +
      "        this, arguments);" +
      "  };" +
      "}" +
      "function JSCompiler_unstubMethod(" +
      "    JSCompiler_unstubMethod_id, JSCompiler_unstubMethod_body) {" +
      "  return JSCompiler_stubMap[JSCompiler_unstubMethod_id] = " +
      "      JSCompiler_unstubMethod_body;" +
      "}", 
      CrossModuleMethodMotion.STUB_DECLARATIONS);
  }
}