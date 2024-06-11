import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.JSTYPE;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createParameters;
import static com.google.javascript.rhino.NodeFactory.createTypeNode;
import static com.google.javascript.rhino.JSType.createArrowType;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class ArrowTypeTest {

 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testArrowTypeCreation() {
 // Test empty arrow function
 Node emptyParameters = null;
 JSType emptyReturnType = JSTYPE.VOID;
 ArrowType emptyArrowType = new ArrowType(registry, emptyParameters, emptyReturnType);
 assertEquals(emptyArrowType.getClass(), ArrowType.class);
 // Test arrow function with parameters and return type
 Node parameters = createParameters(createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.STRING));
 JSType returnType = createTypeNode(JSTYPE.BOOL);
 ArrowType arrowType = new ArrowType(registry, parameters, returnType);
 assertEquals(arrowType.getClass(), ArrowType.class);
 assertEquals(arrowType.parameters, parameters);
 assertEquals(arrowType.returnType, returnType);
 }

 @Test
 public void testIsSubtype() {
 // Test subtyping between arrow types with the same parameter and return type
 ArrowType arrowType1 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType2 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 assertTrue(arrowType1.isSubtype(arrowType2));
 assertTrue(arrowType2.isSubtype(arrowType1));
 // Test subtyping between arrow types with a super return type
 ArrowType arrowType3 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType4 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.NUMBER));
 assertTrue(arrowType3.isSubtype(arrowType4));
 // Test subtyping between arrow types with a sub parameter type
 ArrowType arrowType5 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.NUMBER));
 ArrowType arrowType6 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 assertTrue(arrowType5.isSubtype(arrowType6));
 // Test subtyping between arrow types with a missing parameter
 ArrowType arrowType7 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType8 = createArrowType(registry, null, createTypeNode(JSTYPE.BOOL));
 assertTrue(arrowType7.isSubtype(arrowType8));
 // Test subtyping between arrow types with a variable number of arguments
 ArrowType arrowType9 = createArrowType(registry, createParametersWithVarArgs(createTypeNode(JSTYPE.INT)), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType10 = createArrowType(registry, createParametersWithVarArgs(createTypeNode(JSTYPE.NUMBER)), createTypeNode(JSTYPE.BOOL));
 assertTrue(arrowType9.isSubtype(arrowType10));
 // Test non-subtyping between arrow types with different return types
 ArrowType arrowType11 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType12 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.NUMBER));
 assertFalse(arrowType11.isSubtype(arrowType12));
 // Test non-subtyping between arrow types with different parameter types
 ArrowType arrowType13 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType14 = createArrowType(registry, createTypeNode(JSTYPE.STRING), createTypeNode(JSTYPE.BOOL));
 assertFalse(arrowType13.isSubtype(arrowType14));
 }

 @Test
 public void testHasEqualParameters() {
 ArrowType arrowType1 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType2 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType3 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.STRING));
 ArrowType arrowType4 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType5 = createArrowType(registry, createTypeNode(JSTYPE.STRING), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType6 = createArrowType(registry, createParametersWithVarArgs(createTypeNode(JSTYPE.INT)), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType7 = createArrowType(registry, createParametersWithVarArgs(createTypeNode(JSTYPE.NUMBER)), createTypeNode(JSTYPE.BOOL));
 assertTrue(arrowType1.hasEqualParameters(arrowType1));
 assertTrue(arrowType1.hasEqualParameters(arrowType2));
 assertFalse(arrowType1.hasEqualParameters(arrowType3));
 assertFalse(arrowType1.hasEqualParameters(arrowType4));
 assertFalse(arrowType1.hasEqualParameters(arrowType5));
 assertFalse(arrowType1.hasEqualParameters(arrowType6));
 assertFalse(arrowType6.hasEqualParameters(arrowType1));
 assertFalse(arrowType6.hasEqualParameters(arrowType7));
 }

 @Test
 public void testEquivalenceAndHashCode() {
 ArrowType arrowType1 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType2 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType3 = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.STRING));
 ArrowType arrowType4 = createArrowType(registry, createTypeNode(JSTYPE.STRING), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType5 = createArrowType(registry, createParametersWithVarArgs(createTypeNode(JSTYPE.INT)), createTypeNode(JSTYPE.BOOL));
 ArrowType arrowType6 = createArrowType(registry, createParametersWithVarArgs(createTypeNode(JSTYPE.NUMBER)), createTypeNode(JSTYPE.BOOL));
 assertEquals(arrowType1, arrowType1);
 assertEquals(arrowType1, arrowType2);
 assertNotEquals(arrowType1, arrowType3);
 assertNotEquals(arrowType1, arrowType4);
 assertNotEquals(arrowType1, arrowType5);
 assertNotEquals(arrowType1, arrowType6);
 assertNotEquals(arrowType5, arrowType6);
 int hashCode1 = arrowType1.hashCode();
 int hashCode2 = arrowType2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLeastSupertype() {
 ArrowType arrowType = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 arrowType.getLeastSupertype(createArrowType(registry, createTypeNode(JSTYPE.STRING), createTypeNode(JSTYPE.BOOL)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetGreatestSubtype() {
 ArrowType arrowType = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 arrowType.getGreatestSubtype(createArrowType(registry, createTypeNode(JSTYPE.STRING), createTypeNode(JSTYPE.BOOL)));
 }

 @Test
 public void testVisit() {
 Visitor<Object> visitor = creating -> null;
 ArrowType arrowType = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 assertEquals(null, arrowType.visit(visitor));
 }

 @Test
 public void testToString() {
 ArrowType arrowType = createArrowType(registry, createTypeNode(JSTYPE.INT), createTypeNode(JSTYPE.BOOL));
 assertEquals("function(int)bool", arrowType.toString());
 }

 private Node createParametersWithVarArgs(Node type) {
 Node parameters = createParameters(type, createTypeNode(JSTypeNative.ANY_ARROW));
 return parameters;
 }

 private Node createParameters(Node... types) {
 return createNode(JSTypeNative.FUNCTION, types);
 }
}