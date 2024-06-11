import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;

public class ArrowTypeTest {

 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testArrowTypeCreation() {
 Node params = Node.newList();
 params.add(Node.newIdent("x"));
 params.add(Node.newIdent("y"));
 Node returnTypeNode = Node.newIdent("number");
 ArrowType arrowType = new ArrowType(registry, params, returnTypeNode);
 assertEquals("function(x, y) -> number", arrowType.toString());
 }

 @Test
 public void testArrowTypeCreationWithEmptyParameterList() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 assertEquals("function() -> ", arrowType.toString());
 }

 @Test
 public void testArrowTypeCreationWithVariableParamList() {
 Node params = Node.newList();
 params.add(Node.newIdent("x"));
 params.add(Node.newIdent("y"));
 params.add(Node.newIdent("...args"));
 Node returnTypeNode = Node.newIdent("number");
 ArrowType arrowType = new ArrowType(registry, params, returnTypeNode);
 assertEquals("function(x, y, ...args) -> number", arrowType.toString());
 }

 @Test
 public void testArrowTypeCreationWithInferredReturnType() {
 Node params = Node.newList();
 params.add(Node.newIdent("x"));
 Node returnTypeNode = Node.newVar("ret");
 ArrowType arrowType = new ArrowType(registry, params, returnTypeNode, true);
 assertEquals("function(x) -> <inferred>", arrowType.toString());
 }

 @Test
 public void testArrowTypeCreationWithEmptyInferredReturnType() {
 ArrowType arrowType = new ArrowType(registry, null, null, true);
 assertEquals("function() -> <inferred>", arrowType.toString());
 }

 @Test
 public void testArrowTypeEquality() {
 Node params1 = Node.newList();
 params1.add(Node.newIdent("x"));
 params1.add(Node.newIdent("y"));
 Node returnTypeNode1 = Node.newIdent("number");
 ArrowType arrowType1 = new ArrowType(registry, params1, returnTypeNode1);
 Node params2 = Node.newList();
 params2.add(Node.newIdent("x"));
 params2.add(Node.newIdent("z"));
 Node returnTypeNode2 = Node.newIdent("string");
 ArrowType arrowType2 = new ArrowType(registry, params2, returnTypeNode2);
 ArrowType arrowType3 = new ArrowType(registry, params1, returnTypeNode1);

 assertTrue(arrowType1.equals(arrowType1));
 assertTrue(arrowType1.equals(arrowType3));
 assertFalse(arrowType1.equals(arrowType2));
 }

 @Test
 public void testArrowTypeHashCode() {
 Node params = Node.newList();
 params.add(Node.newIdent("x"));
 params.add(Node.newIdent("y"));
 Node returnTypeNode = Node.newIdent("number");
 ArrowType arrowType1 = new ArrowType(registry, params, returnTypeNode);
 ArrowType arrowType2 = new ArrowType(registry, params, returnTypeNode);
 assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
 }

 @Test
 public void testArrowTypeSubtyping() {
 Node params1 = Node.newList();
 params1.add(Node.newIdent("x"));
 params1.add(Node.newIdent("y"));
 Node returnTypeNode1 = Node.newIdent("number");
 ArrowType arrowType1 = new ArrowType(registry, params1, returnTypeNode1);
 Node params2 = Node.newList();
 params2.add(Node.newIdent("x"));
 Node returnTypeNode2 = Node.newIdent("number");
 ArrowType arrowType2 = new ArrowType(registry, params2, returnTypeNode2);
 Node params3 = Node.newList();
 params3.add(Node.newIdent("x"));
 params3.add(Node.newIdent("y"));
 params3.add(Node.newIdent("z"));
 Node returnTypeNode3 = Node.newIdent("number");
 ArrowType arrowType3 = new ArrowType(registry, params3, returnTypeNode3);
 Node params4 = Node.newList();
 params4.add(Node.newIdent("x"));
 params4.add(Node.newIdent("z"));
 Node returnTypeNode4 = Node.newIdent("number");
 ArrowType arrowType4 = new ArrowType(registry, params4, returnTypeNode4);
 Node params5 = Node.newList();
 params5.add(Node.newIdent("x"));
 Node returnTypeNode5 = Node.newIdent("string");
 ArrowType arrowType5 = new ArrowType(registry, params1, returnTypeNode5);

 assertTrue(arrowType1.isSubtype(arrowType1));
 assertTrue(arrowType1.isSubtype(arrowType2));
 assertFalse(arrowType1.isSubtype(arrowType3));
 assertFalse(arrowType1.isSubtype(arrowType4));
 assertFalse(arrowType1.isSubtype(arrowType5));
 }

 @Test
 public void testArrowTypeParameterEquality() {
 Node params1 = Node.newList();
 params1.add(Node.newIdent("x"));
 params1.add(Node.newIdent("y"));
 Node params2 = Node.newList();
 params2.add(Node.newIdent("x"));
 params2.add(Node.newIdent("z"));
 ArrowType arrowType1 = new ArrowType(registry, params1, Node.newIdent("number"));
 ArrowType arrowType2 = new ArrowType(registry, params2, Node.newIdent("number"));
 assertFalse(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testArrowTypeParameterEqualityWithIdenticalParameterLists() {
 Node params1 = Node.newList();
 params1.add(Node.newIdent("x"));
 params1.add(Node.newIdent("y"));
 Node params2 = Node.newList();
 params2.add(Node.newIdent("x"));
 params2.add(Node.newIdent("y"));
 ArrowType arrowType1 = new ArrowType(registry, params1, Node.newIdent("number"));
 ArrowType arrowType2 = new ArrowType(registry, params2, Node.newIdent("number"));
 assertTrue(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testArrowTypeParameterEqualityWithVarArgs() {
 Node params1 = Node.newList();
 params1.add(Node.newIdent("x"));
 params1.add(Node.newIdent("y"));
 params1.add(Node.newIdent("...args"));
 Node params2 = Node.newList();
 params2.add(Node.newIdent("x"));
 params2.add(Node.newIdent("y"));
 params2.add(Node.newIdent("...args"));
 ArrowType arrowType1 = new ArrowType(registry, params1, Node.newIdent("number"));
 ArrowType arrowType2 = new ArrowType(registry, params2, Node.newIdent("number"));
 assertTrue(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testArrowTypeToString() {
 Node params = Node.newList();
 params.add(Node.newIdent("x"));
 params.add(Node.newIdent("y"));
 Node returnTypeNode = Node.newIdent("number");
 ArrowType arrowType = new ArrowType(registry, params, returnTypeNode);
 assertEquals("function(x, y) -> number", arrowType.toString());
 }

 @Test
 public void testArrowTypeWithUnknownTypes() {
 Node params = Node.newList();
 params.add(Node.newIdent("x"));
 params.add(Node.newIdent("y"));
 ArrowType arrowType = new ArrowType(registry, params, Node.newUndefinedType());
 assertEquals("function(x, y) -> undefined", arrowType.toString());
 }
}