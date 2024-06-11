import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeVisitor;

public class ArrowTypeTest {

 private static JSTypeRegistry registry = new JSTypeRegistry();

 @Test
 public void testArrowType() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 JSType nativeType = registry.createNativeType(JSTypeNative.VOID);
 FunctionType functionType = new FunctionType(registry, null, nativeType);
 ArrowType arrowTypeWithFunctionType = new ArrowType(registry, null, functionType);

 // Test isSubtype()
 assertTrue(arrowType.isSubtype(arrowType));
 assertTrue(arrowType.isSubtype(arrowTypeWithFunctionType));
 assertFalse(arrowTypeWithFunctionType.isSubtype(arrowType));

 // Test hasEqualParameters()
 ArrowType arrowType1 = new ArrowType(registry, null, null, true, null);
 ArrowType arrowType2 = new ArrowType(registry, null, null, true, null);
 ArrowType arrowType3 = new ArrowType(registry, null, null, false, null);
 ArrowType arrowType4 = new ArrowType(registry, null, null, true, new Node(null, null));
 ArrowType arrowType5 = new ArrowType(registry, null, functionType, false, null);
 ArrowType arrowType6 = new ArrowType(registry, null, new Node(null, null), false, null);

 assertTrue(arrowType1.hasEqualParameters(arrowType1, true));
 assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
 assertFalse(arrowType1.hasEqualParameters(arrowType3, true));
 assertFalse(arrowType1.hasEqualParameters(arrowType4, true));
 assertFalse(arrowType1.hasEqualParameters(arrowType5, true));
 assertFalse(arrowType1.hasEqualParameters(arrowType6, true));

 // Test hashCode()
 ArrowType arrowType7 = new ArrowType(registry, null, null);
 ArrowType arrowType8 = new ArrowType(registry, null, null);
 ArrowType arrowType9 = new ArrowType(registry, null, registry.createNativeType(JSTypeNative.NUMBER));
 ArrowType arrowType10 = new ArrowType(registry, null, registry.createNativeType(JSTypeNative.NUMBER), true);

 assertEquals(arrowType7.hashCode(), arrowType7.hashCode());
 assertEquals(arrowType7.hashCode(), arrowType8.hashCode());
 assertNotEquals(arrowType7.hashCode(), arrowType9.hashCode());
 assertNotEquals(arrowType7.hashCode(), arrowType10.hashCode());
 }

 @Test
 public void testResolve() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 Node node = new Node(null, null);
 JSType expectedType = registry.createNativeType(JSTypeNative.NUMBER);
 node.setJSType(expectedType);
 ArrowType arrowTypeWithExpectedType = new ArrowType(registry, null, expectedType);
 ArrowType arrowTypeWithNode = new ArrowType(registry, null, node);

 ArrowType resolvedArrowType = arrowType.resolve(null, null);
 assertEquals(arrowType, resolvedArrowType);

 JSType resolvedType = resolvedArrowType.returnType.resolve(null, null);
 assertEquals(registry.createNativeType(JSTypeNative.UNDEFINED), resolvedType);

 ArrowType resolvedArrowTypeWithNode = arrowTypeWithNode.resolve(null, null);
 assertEquals(arrowTypeWithNode, resolvedArrowTypeWithNode);

 ArrowType resolvedArrowTypeWithExpectedType = arrowTypeWithExpectedType.resolve(null, null);
 assertEquals(arrowTypeWithExpectedType, resolvedArrowTypeWithExpectedType);

 JSType resolvedReturnType = resolvedArrowTypeWithExpectedType.returnType.resolve(null, null);
 assertEquals(expectedType, resolvedReturnType);
 }

 @Test
 public void testCheckEquivalenceHelper() {
 ArrowType arrowType1 = new ArrowType(registry, null, null);
 ArrowType arrowType2 = new ArrowType(registry, null, null);
 ArrowType arrowType3 = new ArrowType(registry, null, registry.createNativeType(JSTypeNative.VOID));
 ArrowType arrowType4 = new ArrowType(registry, null, registry.createNativeType(JSTypeNative.NUMBER));
 ArrowType arrowType5 = new ArrowType(registry, null, null, true);
 ArrowType arrowType6 = new ArrowType(registry, null, functionType, false);
 ArrowType arrowType7 = new ArrowType(registry, null, null, true, new Node(null, null));
 ArrowType arrowType8 = new ArrowType(registry, null, null, false, new Node(null, null));
 ArrowType arrowType9 = new ArrowType(registry, null, registry.createNativeType(JSTypeNative.BOOLEAN), true);
 ArrowType arrowType10 = new ArrowType(registry, null, registry.createNativeType(JSTypeNative.OBJECT), true);
 ArrowType arrowType11 = new ArrowType(registry, null, null, true, new Node(null, new Node(null, new Node(null, null))));

 assertTrue(arrowType1.checkEquivalenceHelper(arrowType1, true));
 assertTrue(arrowType1.checkEquivalenceHelper(arrowType2, true));
 assertFalse(arrowType1.checkEquivalenceHelper(arrowType3, true));
 assertFalse(arrowType1.checkEquivalenceHelper(arrowType4, true));
 assertFalse(arrowType1.checkEquivalenceHelper(arrowType5, true));
 assertFalse(arrowType1.checkEquivalenceHelper(arrowType6, true));
 assertFalse(arrowType1.checkEquivalenceHelper(arrowType7, true));
 assertFalse(arrowType1.checkEquivalenceHelper(arrowType8, true));
 assertFalse(arrowType1.checkEquivalenceHelper(arrowType9, true));
 assertFalse(arrowType1.checkEquivalenceHelper(arrowType10, true));
 assertFalse(arrowType1.checkEquivalenceHelper(arrowType11, true));

 // Test with null parameters
 JSTypeRegistry registry2 = new JSTypeRegistry();
 ArrowType arrowType12 = new ArrowType(registry2, null, null);
 ArrowType arrowType13 = null;

 // Test same ArrowType
 assertTrue(arrowType12.checkEquivalenceHelper(arrowType12, true));
 // Test null ArrowType
 assertFalse(arrowType12.checkEquivalenceHelper(arrowType13, true));
 // Test different ArrowType with different returnType
 ArrowType arrowType14 = new ArrowType(registry2, null, registry.createNativeType(JSTypeNative.STRING));
 assertFalse(arrowType12.checkEquivalenceHelper(arrowType14, true));
 // Test different ArrowType with different parameters
 ArrowType arrowType15 = new ArrowType(registry2, new Node(null, new Node(null, null)), null);
 assertFalse(arrowType12.checkEquivalenceHelper(arrowType15, true));
 }

 @Test
 public void testToString() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 assertEquals("[ArrowType]", arrowType.toStringHelper(false));
 }
}