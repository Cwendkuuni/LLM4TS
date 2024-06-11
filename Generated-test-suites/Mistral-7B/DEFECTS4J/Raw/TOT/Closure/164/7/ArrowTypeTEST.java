import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.test.JSTestCase;

public class ArrowTypeTest extends JSTestCase {

 private static final ErrorReporter t = new ErrorReporter();
 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry(t);
 }

 @Test
 public void testArrowTypeWithParametersAndReturnType() {
 ArrowType arrowType = new ArrowType(registry,
 new Node("(int, int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));

 assertTrue(arrowType.isSubtype(new ArrowType(registry,
 new Node("(int, int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE))));

 assertTrue(arrowType.isSubtype(new ArrowType(registry,
 new Node("(int, int, int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE))));

 // Covariant return type
 ArrowType arrowTypeCovariant = new ArrowType(registry,
 new Node("(int, int)"),
 new JSTypeNative(registry, JSTypeNative.STRING_TYPE));

 assertTrue(arrowType.isSubtype(arrowTypeCovariant));

 // Contravariant parameters
 ArrowType arrowTypeContravariant = new ArrowType(registry,
 new Node("(String, int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));

 assertTrue(arrowType.isSubtype(arrowTypeContravariant));
 }

 @Test
 public void testArrowTypeWithUnknownType() {
 ArrowType arrowType = new ArrowType(registry,
 new Node("(int, int)"),
 new JSTypeNative(registry, JSTypeNative.UNKNOWN_TYPE));

 assertFalse(arrowType.isSubtype(new ArrowType(registry,
 new Node("(int, int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE))));
 }

 @Test
 public void testArrowTypeWithUnknownReturnType() {
 ArrowType arrowType = new ArrowType(registry,
 new Node("(int, int)"),
 new JSTypeNative(registry, JSTypeNative.UNKNOWN_TYPE));

 ArrowType arrowTypeWithIntReturn = new ArrowType(registry,
 new Node("(int, int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));

 assertFalse(arrowType.isSubtype(arrowTypeWithIntReturn));
 }

 @Test
 public void testArrowTypeWithMismatchedParameters() {
 ArrowType arrowType = new ArrowType(registry,
 new Node("(int, int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));

 ArrowType arrowTypeMismatched = new ArrowType(registry,
 new Node("(String, int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));

 assertFalse(arrowType.isSubtype(arrowTypeMismatched));
 }

 @Test
 public void testArrowTypeWithMissingParameters() {
 ArrowType arrowType = new ArrowType(registry,
 new Node("(int, int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));

 ArrowType arrowTypeMissingParam = new ArrowType(registry,
 new Node("(int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));

 assertFalse(arrowType.isSubtype(arrowTypeMissingParam));
 }

 @Test
 public void testArrowTypeWithVarArgs() {
 ArrowType arrowType = new ArrowType(registry,
 new Node("(int, int...)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));

 ArrowType arrowTypeVarArgs = new ArrowType(registry,
 new Node("(int, int, int...)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));

 assertTrue(arrowType.isSubtype(arrowTypeVarArgs));
 }

 @Test
 public void testArrowTypeWithMissingAndExtraParameters() {
 ArrowType arrowType = new ArrowType(registry,
 new Node("(int, int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));

 ArrowType arrowTypeMissingParam = new ArrowType(registry,
 new Node("(int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));
 ArrowType arrowTypeExtraParam = new ArrowType(registry,
 new Node("(int, int, int)"),
 new JSTypeNative(registry, JSTypeNative.INT_TYPE));

 assertFalse(arrowType.isSubtype(arrowTypeMissingParam));
 assertFalse(arrowType.isSubtype(arrowTypeExtraParam));
 }
}