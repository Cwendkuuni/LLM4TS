import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;

public class ArrowTypeTest {

 private JSTypeRegistry registry = new JSTypeRegistry();
 private ErrorReporter errorReporter = new ErrorReporter() {};

 @Test
 public void testArrowTypeConstruction() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 assertNull(arrowType.parameters);
 assertNull(arrowType.returnType);
 assertFalse(arrowType.returnTypeInferred);

 ArrowType arrowTypeWithParams = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), null);
 assertEquals(registry.createParametersWithVarArgs(JSTypeNative.INT_TYPE), arrowTypeWithParams.parameters);
 assertNull(arrowTypeWithParams.returnType);
 assertFalse(arrowTypeWithParams.returnTypeInferred);

 ArrowType arrowTypeWithParamsAndReturn = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), new Node(JSTypeNative.STRING_TYPE));
 assertEquals(registry.createParametersWithVarArgs(JSTypeNative.INT_TYPE), arrowTypeWithParamsAndReturn.parameters);
 assertEquals(JSTypeNative.STRING_TYPE, arrowTypeWithParamsAndReturn.returnType);
 assertFalse(arrowTypeWithParamsAndReturn.returnTypeInferred);

 ArrowType arrowTypeWithReturnInferred = new ArrowType(registry, null, new Node(JSTypeNative.BOOL_TYPE), true);
 assertNull(arrowTypeWithReturnInferred.parameters);
 assertNull(arrowTypeWithReturnInferred.returnType);
 assertTrue(arrowTypeWithReturnInferred.returnTypeInferred);
 }

 @Test
 public void testIsSubtype() {
 ArrowType arrowType1 = new ArrowType(registry, null, null);
 ArrowType arrowType2 = new ArrowType(registry, null, null);
 ArrowType arrowType3 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), null);
 ArrowType arrowType4 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), JSTypeNative.STRING_TYPE);
 ArrowType arrowType5 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), null, true);

 assertTrue(arrowType1.isSubtype(arrowType1));
 assertFalse(arrowType1.isSubtype(arrowType2));
 assertFalse(arrowType2.isSubtype(arrowType1));
 assertFalse(arrowType1.isSubtype(arrowType3));
 assertFalse(arrowType3.isSubtype(arrowType1));
 assertFalse(arrowType1.isSubtype(arrowType4));
 assertTrue(arrowType4.isSubtype(arrowType1));
 assertFalse(arrowType1.isSubtype(arrowType5));
 assertFalse(arrowType5.isSubtype(arrowType1));

 ArrowType arrowType6 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), null);
 ArrowType arrowType7 = new ArrowType(registry, new Node(JSTypeNative.INT_ARROW_TYPE), null);
 assertTrue(arrowType6.isSubtype(arrowType7));
 }

 @Test
 public void testHasEqualParameters() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), null);
 ArrowType arrowType2 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), null);
 ArrowType arrowType3 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), new Node(JSTypeNative.STRING_TYPE));
 ArrowType arrowType4 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), null, true);
 ArrowType arrowType5 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), new Node(JSTypeNative.INT_TYPE));
 ArrowType arrowType6 = new ArrowType(registry, new Node(JSTypeNative.INT_ARRAY_TYPE), null);

 assertTrue(arrowType1.hasEqualParameters(arrowType1, false));
 assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
 assertFalse(arrowType1.hasEqualParameters(arrowType3, false));
 assertFalse(arrowType1.hasEqualParameters(arrowType4, false));
 assertFalse(arrowType1.hasEqualParameters(arrowType5, false));
 assertFalse(arrowType1.hasEqualParameters(arrowType6, false));

 assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
 assertTrue(arrowType1.hasEqualParameters(arrowType3, true));
 assertTrue(arrowType1.hasEqualParameters(arrowType4, true));
 assertTrue(arrowType1.hasEqualParameters(arrowType5, true));
 assertFalse(arrowType1.hasEqualParameters(arrowType6, true));

 ArrowType arrowType7 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), null, true);
 ArrowType arrowType8 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), null, true);
 assertTrue(arrowType1.hasEqualParameters(arrowType7, false));
 assertTrue(arrowType1.hasEqualParameters(arrowType8, false));
 assertTrue(arrowType1.hasEqualParameters(arrowType7, true));
 assertTrue(arrowType1.hasEqualParameters(arrowType8, true));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLeastSupertype() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 arrowType.getLeastSupertype(null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetGreatestSubtype() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 arrowType.getGreatestSubtype(null);
 }

 @Test
 public void testToString() {
 ArrowType arrowType = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), null);
 assertEquals("[ArrowType]", arrowType.toStringHelper(false));
 }

 @Test
 public void testHashCode() {
 ArrowType arrowType1 = new ArrowType(registry, null, null);
 ArrowType arrowType2 = new ArrowType(registry, null, null);
 ArrowType arrowType3 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), null);
 ArrowType arrowType4 = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE), JSTypeNative.STRING_TYPE);

 assertEquals(hashCode(arrowType1), hashCode(arrowType1));
 assertEquals(hashCode(arrowType1), hashCode(arrowType2));
 assertNotEquals(hashCode(arrowType1), hashCode(arrowType3));
 assertNotEquals(hashCode(arrowType1), hashCode(arrowType4));
 }

 private int hashCode(ArrowType arrowType) {
 int hashCode = 0;
 if (arrowType.returnType != null) {
 hashCode += arrowType.returnType.hashCode();
 }
 if (arrowType.returnTypeInferred) {
 hashCode += 1;
 }
 if (arrowType.parameters != null) {
 Node param = arrowType.parameters.getFirstChild();
 while (param != null) {
 JSType paramType = param.getJSType();
 if (paramType != null) {
 hashCode += paramType.hashCode();
 }
 param = param.getNext();
 }
 }
 return hashCode;
 }
}