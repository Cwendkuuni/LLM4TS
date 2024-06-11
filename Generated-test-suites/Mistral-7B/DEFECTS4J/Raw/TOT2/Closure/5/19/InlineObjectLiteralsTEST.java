import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InlineObjectLiteralsTest {

 private Compiler compiler;
 private InlineObjectLiterals inlineObjectLiterals;

 @Before
 public void setup() {
 compiler = new Compiler();
 inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tempVariable");
 }

 @Test
 public void testInlineObjectLiterals() {
 Node rootNode = Node.objectLiteral(
 Node.PROPERTIES,
 Node.PROPERTY(Node.IDENT("key1"), Node.STRING("value1")),
 Node.PROPERTY(Node.IDENT("key2"), Node.OBJECT_LITERAL(
 Node.PROPERTIES,
 Node.PROPERTY(Node.IDENT("nestedKey1"), Node.STRING("nestedValue1")),
 Node.PROPERTY(Node.IDENT("nestedKey2"), Node.STRING("nestedValue2"))
 ))
 );

 inlineObjectLiterals.process(Node.SCOPE_GLOBAL, rootNode);
 Node result = compiler.getOutput();

 assertEquals(
 "var key1 = \"value1\";\n" +
 "var nestedKey1 = \"nestedValue1\";\n" +
 "var nestedKey2 = \"nestedValue2\";\n" +
 "var key2 = {\n" +
 "  nestedKey1: nestedKey1,\n" +
 "  nestedKey2: nestedKey2\n" +
 "};\n" +
 "key1 = key2;\n",
 result.toString()
 );
 }

 @Test
 public void testInlineObjectLiteralsWithInitialValues() {
 Node rootNode = Node.objectLiteral(
 Node.PROPERTIES,
 Node.PROPERTY(Node.IDENT("key1"), Node.STRING("value1")),
 Node.PROPERTY(Node.IDENT("key2"), Node.OBJECT_LITERAL(
 Node.PROPERTIES,
 Node.PROPERTY(Node.IDENT("nestedKey1"), Node.STRING("nestedValue1")),
 Node.PROPERTY(Node.IDENT("nestedKey2"), Node.STRING("nestedValue2")),
 Node.IS(Token.EQUALS, Node.NIL())
 ))
 );

 inlineObjectLiterals.process(Node.SCOPE_GLOBAL, rootNode);
 Node result = compiler.getOutput();

 assertEquals(
 "var key1 = \"value1\";\n" +
 "var key2 = {\n" +
 "  nestedKey1: \"nestedValue1\",\n" +
 "  nestedKey2: \"nestedValue2\",\n" +
 "  nestedKey1: null,\n" +
 "  nestedKey2: null\n" +
 "};\n" +
 "key1 = key2;\n",
 result.toString()
 );
 }

 @Test
 public void testInlineObjectLiteralsWithCyclicReference() {
 Node rootNode = Node.objectLiteral(
 Node.PROPERTIES,
 Node.PROPERTY(Node.IDENT("obj1"), Node.OBJECT_LITERAL(
 Node.PROPERTIES,
 Node.PROPERTY(Node.IDENT("ref1"), Node.OBJECT_LITERAL(
 Node.PROPERTIES,
 Node.PROPERTY(Node.IDENT("ref2"), Node.IDENT("obj1"))
 ))
 ))
 );

 try {
 inlineObjectLiterals.process(Node.SCOPE_GLOBAL, rootNode);
 fail("Should not be able to inline object literals with cyclic references.");
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testInlineObjectLiteralsWithExportedVariable() {
 Node rootNode = Node.objectLiteral(Node.IDENT("exportedVar"), Node.OBJECT_LITERAL(
 Node.PROPERTIES,
 Node.PROPERTY(Node.IDENT("key"), Node.STRING("value"))
 ));

 inlineObjectLiterals.process(Node.SCOPE_GLOBAL, rootNode);
 Node result = compiler.getOutput();

 assertEquals(
 "var exportedVar = {\n" +
 "  key: \"value\"\n" +
 "};\n",
 result.toString()
 );

 assertEquals("exportedVar.key", "value");
 }
}