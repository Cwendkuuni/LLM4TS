import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass.GetExternMethods;
import com.google.javascript.jscomp.MethodCompilerPass.GatherSignatures;
import com.google.javascript.jscomp.MethodCompilerPass.SignatureStore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MethodCompilerPassTest {

 interface TestMethodCompilerPass extends MethodCompilerPass {
 SignatureStore signatureStore = new SignatureStore() {
 @Override
 public void reset() {}

 @Override
 public void addSignature(String functionName, Node functionNode, String sourceFile) {
 signatures.put(functionName, functionNode);
 }

 @Override
 public void removeSignature(String functionName) {
 signatures.remove(functionName);
 }

 private Multimap<String, Node> signatures = HashMultimap.create();
 };

 @Override
 abstract Callback getActingCallback();

 @Override
 abstract SignatureStore getSignatureStore();

 @Override
 void process(Node externs, Node root) {}

 @Override
 Callback getActingCallback() {
 return new Callback() {
 @Override
 public void call(Node n) {}
 };
 }

 @Override
 SignatureStore getSignatureStore() {
 return signatureStore;
 }
 };

 @Test
 public void testExtractPublicMethods() {
 TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
 // No methods are extracted from null
 assertNotNull(pass.externMethods);
 assertFalse(pass.externMethods.isEmpty());
 // Methods are extracted from the given Node
 Node externsNode = createNode("externs: { foo: function() {}, bar: function(arg) {} }");
 pass.process(externsNode, null);
 assertEquals(2, pass.externMethods.size());
 assertEquals("foo", pass.externMethods.iterator().next());
 assertEquals("bar", pass.externMethods.iterator().next());
 }

 @Test
 public void testAddSignature() {
 TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
 pass.process(null, null);
 Node functionNode = createNode("function foo() {}");
 pass.addSignature("foo", functionNode, "test.js");
 Node expectedFunctionNode = pass.signatures.get("foo");
 assertEquals(functionNode, expectedFunctionNode);
 }

 @Test
 public void testRemoveSignature() {
 TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
 pass.process(null, null);
 Node functionNode = createNode("function foo() {}");
 pass.addSignature("foo", functionNode, "test.js");
 pass.removeSignature("foo");
 assertNull(pass.signatures.get("foo"));
 }

 @Test
 public void testAddAndRemoveSignatures() {
 TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
 pass.process(null, null);
 Node functionNode1 = createNode("function foo() {}");
 pass.addSignature("foo", functionNode1, "test1.js");
 Node functionNode2 = createNode("function foo() {}");
 pass.addSignature("foo", functionNode2, "test2.js");
 assertEquals(2, pass.signatures.size());
 pass.removeSignature("foo");
 assertEquals(0, pass.signatures.size());
 }

 @Test
 public void testMethodsWithoutSignatures() {
 TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
 pass.process(null, null);
 pass.addSignature("foo", createNode("function foo() {}"), "test.js");
 pass.externMethodsWithoutSignatures.add("bar");
 assertEquals(2, pass.externMethods.size() + pass.externMethodsWithoutSignatures.size());
 assertEquals("foo", pass.externMethods.iterator().next());
 assertEquals("bar", pass.externMethodsWithoutSignatures.iterator().next());
 }

 @Test
 public void testNonMethodProperties() {
 TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
 pass.process(null, null);
 pass.addSignature("foo", createNode("function foo() {}"), "test.js");
 pass.addPossibleSignature("prototype", null, null);
 assertEquals(1, pass.externMethods.size());
 assertEquals(1, pass.nonMethodProperties.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testUndefinedFunction() {
 TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
 pass.process(null, null);
 Node undefinedFunction = createNode("undefinedFunction");
 pass.addPossibleSignature("undefinedFunction", undefinedFunction, null);
 }

 @Test
 public void testGetExternMethods() {
 TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
 Node externsNode = createNode("externs: { foo: function() {}, bar: function(arg) {}, baz: {} }");
 pass.process(externsNode, null);
 GetExternMethods getExternMethods = pass.new GetExternMethods();
 assertEquals(3, getExternMethods.signatures.size());
 assertEquals("foo", getExternMethods.signatures.get("foo").getName());
 assertEquals("bar", getExternMethods.signatures.get("bar").getName());
 assertNull(getExternMethods.signatures.get("baz"));
 }

 @Test
 public void testGatherSignatures() {
 TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
 pass.process(null, null);
 Node functionNode1 = createNode("function foo() {}");
 Node functionNode2 = createNode("function bar(arg) {}");
 Node parentNode1 = createNode("assign");
 Node parentNode2 = createNode("getprop");
 parentNode1.addChild(parentNode2);
 parentNode2.addChild(createNode("name").setString("Foo"));
 parentNode2.addChild(createNode("string").setString("prototype"));
 parentNode2.addChild(createNode("string").setString("getFoo"));
 parentNode2.addChild(functionNode1);
 parentNode1.addChild(parentNode2);
 parentNode2 = createNode("getprop");
 parentNode2.addChild(createNode("name").setString("Foo"));
 parentNode2.addChild(createNode("string").setString("prototype"));
 parentNode2.addChild(createNode("string").setString("prototype.getBar"));
 parentNode2.addChild(functionNode2);
 parentNode1.addChild(parentNode2);
 Node rootNode = createNode("program");
 rootNode.addChild(parentNode1);
 pass.process(null, rootNode);
 GatherSignatures gatherSignatures = pass.new GatherSignatures();
 assertEquals(2, gatherSignatures.signatures.size());
 assertEquals("getFoo", gatherSignatures.signatures.get("getFoo").getName());
 assertEquals("getBar", gatherSignatures.signatures.get("getBar").getName());
 }

 @Test
 public void testActingCallback() {
 TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
 Node functionNode = createNode("function foo() {}");
 Callback actingCallback = pass.getActingCallback();
 actingCallback.call(functionNode);
 assertTrue(actingCallback.isCallable());
 }

}