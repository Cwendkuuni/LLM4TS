import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.StaticTypeEnvironment;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArrowTypeTest {

 @Test
 public void testIsSubtype() {
 StaticTypeEnvironment env = new StaticTypeEnvironment();

 // Test arrow type with no parameters and no return type
 ArrowType arrow1 = new ArrowType(JSTypeRegistry.getInstance(), null, null);
 ArrowType arrow2 = new ArrowType(JSTypeRegistry.getInstance(), null, null);
 assertTrue(arrow1.isSubtype(arrow2));

 // Test arrow type with no parameters and a return type
 ArrowType arrow1WithReturn = new ArrowType(JSTypeRegistry.getInstance(), null, getNativeType(int.class));
 ArrowType arrow2WithReturn = new ArrowType(JSTypeRegistry.getInstance(), null, getNativeType(String.class));
 assertFalse(arrow1WithReturn.isSubtype(arrow2WithReturn));

 // Test arrow type with one parameter and no return type
 ArrowType arrow1Param = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), null);
 ArrowType arrow2Param = new ArrowType(JSTypeRegistry.getInstance(), getNode("y"), null);
 assertFalse(arrow1Param.isSubtype(arrow2Param));

 // Test arrow type with one parameter and a return type
 ArrowType arrow1ParamWithReturn = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(int.class));
 ArrowType arrow2ParamWithReturn = new ArrowType(JSTypeRegistry.getInstance(), getNode("y"), getNativeType(String.class));
 assertFalse(arrow1ParamWithReturn.isSubtype(arrow2ParamWithReturn));

 // Test arrow type with parameters of the same type
 ArrowType arrow1ParamSame = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(int.class));
 ArrowType arrow2ParamSame = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(int.class));
 assertTrue(arrow1ParamSame.isSubtype(arrow2ParamSame));

 // Test arrow type with parameters of the same type and a return type
 ArrowType arrow1ParamSameWithReturn = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(int.class));
 ArrowType arrow2ParamSameWithReturn = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(String.class));
 assertFalse(arrow1ParamSameWithReturn.isSubtype(arrow2ParamSameWithReturn));

 // Test arrow type with one parameter and a variable number of arguments
 ArrowType arrow1ParamVarArgs = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), null, true);
 ArrowType arrow2ParamVarArgs = new ArrowType(JSTypeRegistry.getInstance(), getNode("y"), null, true);
 assertTrue(arrow1ParamVarArgs.isSubtype(arrow2ParamVarArgs));

 // Test arrow type with one parameter and a return type, and another arrow type with the same return type and different parameters
 ArrowType arrow1ParamWithReturnAndAnother = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(int.class));
 ArrowType arrow2ParamDifferent = new ArrowType(JSTypeRegistry.getInstance(), getNode("y"), getNativeType(int.class));
 assertFalse(arrow1ParamWithReturnAndAnother.isSubtype(arrow2ParamDifferent));

 // Test arrow type with the same parameters and return type, but one inferred and the other not
 ArrowType arrow1ParamInferred = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), null, true);
 ArrowType arrow2ParamExplicit = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(int.class));
 assertTrue(arrow1ParamInferred.isSubtype(arrow2ParamExplicit));

 // Test arrow type with the same parameters and return type, but different registries
 ArrowType arrow1ParamRegistryOne = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(int.class));
 ArrowType arrow2ParamRegistryTwo = new ArrowType(new JSTypeRegistry(), getNode("x"), getNativeType(int.class));
 assertTrue(TypeEquivalenceTester.areEquivalent(arrow1ParamRegistryOne, arrow2ParamRegistryTwo));
 }

 @Test
 public void testHasEqualParameters() {
 StaticTypeEnvironment env = new StaticTypeEnvironment();

 // Test arrow type with no parameters
 ArrowType arrow1 = new ArrowType(JSTypeRegistry.getInstance(), null, null);
 ArrowType arrow2 = new ArrowType(JSTypeRegistry.getInstance(), null, null);
 assertTrue(arrow1.hasEqualParameters(arrow2));

 // Test arrow type with one parameter
 ArrowType arrow1Param = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), null);
 ArrowType arrow2Param = new ArrowType(JSTypeRegistry.getInstance(), getNode("y"), null);
 assertFalse(arrow1Param.hasEqualParameters(arrow2Param));

 // Test arrow type with one parameter and a return type
 ArrowType arrow1ParamWithReturn = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(int.class));
 ArrowType arrow2ParamWithReturn = new ArrowType(JSTypeRegistry.getInstance(), getNode("y"), getNativeType(String.class));
 assertFalse(arrow1ParamWithReturn.hasEqualParameters(arrow2ParamWithReturn));

 // Test arrow type with same parameters and return type
 ArrowType arrow1ParamSame = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(int.class));
 ArrowType arrow2ParamSame = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(int.class));
 assertTrue(arrow1ParamSame.hasEqualParameters(arrow2ParamSame));

 // Test arrow type with same parameters and different return types
 ArrowType arrow1ParamSameReturnDifferent = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(int.class));
 ArrowType arrow2ParamSameReturnDifferent = new ArrowType(JSTypeRegistry.getInstance(), getNode("x"), getNativeType(String.class));
 assertFalse(arrow1ParamSameReturnDifferent.hasEqualParameters(arrow2ParamSameReturnDifferent));
 }

 private static Node getNode(String name) {
 Node node = new Node();
 node.setIdentifier(name);
 return node;
 }
}