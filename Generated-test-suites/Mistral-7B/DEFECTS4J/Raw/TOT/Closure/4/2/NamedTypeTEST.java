import com.google.javascript.jscomp.info.CompilationInfo;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeFactory;
import com.google.javascript.jscomp.type.TypeUtils;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.PropertyDescriptor;
import com.google.javascript.rhino.jstype.PropertySlot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class NamedTypeTest {

 private static final String NAMED_TYPE_REFERENCE = "MyReferenceType";
 private static final String NAMED_TYPE_SOURCE_NAME = "NamedTypeSource";
 private static final int NAMED_TYPE_LINE_NUMBER = 10;
 private static final int NAMED_TYPE_CHAR_NUMBER = 20;
 private static final int ENUM_LINE_NUMBER = 20;
 private static final int ENUM_CHAR_NUMBER = 30;

 private static final ErrorReporter errorReporter = mock(ErrorReporter.class);
 private static final JSTypeRegistry registry = mock(JSTypeRegistry.class);
 private static final JSTypeFactory typeFactory = mock(JSTypeFactory.class);
 private static final CompilationInfo compilationInfo = mock(CompilationInfo.class);

 @Test
 public void testNamedTypeCreation() {
 NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE,
 NAMED_TYPE_SOURCE_NAME, NAMED_TYPE_LINE_NUMBER, NAMED_TYPE_CHAR_NUMBER);
 assertSame(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
 namedType.getBaseType());
 assertSame(NAMED_TYPE_REFERENCE, namedType.getReferenceName());
 }

 @Test
 public void testNamedTypeDefineProperty() {
 NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE,
 NAMED_TYPE_SOURCE_NAME, NAMED_TYPE_LINE_NUMBER, NAMED_TYPE_CHAR_NUMBER);
 PropertyContinuation propertyContinuation = new PropertyContinuation(
 "propertyName", JSTypeFactory.createStringType(), true, null);
 namedType.defineProperty("propertyName", null, true, null);
 namedType.finishPropertyContinuations();
 ObjectType expectedObjectType = new ObjectType(registry);
 expectedObjectType.defineProperty("propertyName",
 JSTypeFactory.createStringType(), true, null);
 verify(expectedObjectType, times(1)).defineProperty(
 "propertyName", eq(JSTypeFactory.createStringType()), true, any(Node.class));
 }

 @Test
 public void testNamedTypeGetReferencedType() {
 NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE,
 NAMED_TYPE_SOURCE_NAME, NAMED_TYPE_LINE_NUMBER, NAMED_TYPE_CHAR_NUMBER);
 JSType expectedType = JSTypeFactory.createStringType();
 when(registry.getType(NAMED_TYPE_REFERENCE)).thenReturn(expectedType);
 JSType actualType = namedType.getReferencedType();
 verify(registry, times(1)).getType(NAMED_TYPE_REFERENCE);
 assertSame(expectedType, actualType);
 }

 @Test
 public void testNamedTypeResolveViaRegistry() {
 NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE,
 NAMED_TYPE_SOURCE_NAME, NAMED_TYPE_LINE_NUMBER, NAMED_TYPE_CHAR_NUMBER);
 JSType expectedType = JSTypeFactory.createStringType();
 when(registry.getType(NAMED_TYPE_REFERENCE)).thenReturn(expectedType);
 JSType actualType = namedType.resolve(errorReporter, null);
 verify(registry, times(1)).getType(NAMED_TYPE_REFERENCE);
 assertSame(expectedType, actualType);
 }

 @Test
 public void testNamedTypeResolveViaProperties() {
 NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE,
 NAMED_TYPE_SOURCE_NAME, NAMED_TYPE_LINE_NUMBER, NAMED_TYPE_CHAR_NUMBER);
 ObjectType parentObjectType = new ObjectType(registry);
 parentObjectType.setPropertySlot("propertyName",
 JSTypeFactory.createNumberType(), null);
 when(registry.getType(NAMED_TYPE_REFERENCE)).thenReturn(null);
 when(errorReporter.warning(anyString(), anyString(), anyInt(), anyInt()))
 .thenReturn(null);
 JSType actualType = namedType.resolve(errorReporter, null);
 verify(errorReporter, times(1))
 .warning("Bad type annotation. Unknown type " + NAMED_TYPE_REFERENCE,
 NAMED_TYPE_SOURCE_NAME, NAMED_TYPE_LINE_NUMBER, NAMED_TYPE_CHAR_NUMBER);
 assertSame(JSTypeFactory.getNativeType(JSTypeNative.NO_RESOLVED_TYPE),
 actualType);
 }

 @Test
 public void testNamedTypeResolveViaPropertiesWithEnum() {
 EnumType enumType = new EnumType(registry, "MyEnum", null, null, null);
 enumType.setReferencedType(TYPE_OF_MyEnum.MY_ENUM_ELEMENT);
 ObjectType objectType = new ObjectType(registry);
 objectType.setPropertySlot("propertyName",
 JSTypeFactory.createNumberType(), null);
 when(registry.getType(NAMED_TYPE_REFERENCE))
 .thenReturn(objectType);
 when(registry.getType("MyEnum"))
 .thenReturn(enumType);
 when(errorReporter.warning(anyString(), anyString(), anyInt(), anyInt()))
 .thenReturn(null);
 JSType actualType = new NamedType(registry, NAMED_TYPE_REFERENCE,
 NAMED_TYPE_SOURCE_NAME, NAMED_TYPE_LINE_NUMBER, NAMED_TYPE_CHAR_NUMBER)
 .resolve(errorReporter, null);
 verify(registry, times(2))
 .getType(eq("MyEnum"));
 verify(registry, times(1))
 .getType(eq(NAMED_TYPE_REFERENCE));
 assertSame(enumType.getElementsType(), actualType);
 }

 @Test
 public void testNamedTypeWithValidator() {
 NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE,
 NAMED_TYPE_SOURCE_NAME, NAMED_TYPE_LINE_NUMBER, NAMED_TYPE_CHAR_NUMBER);
 Predicate<JSType> validator = JSTypeFactory::isStringType;
 when(registry.getType(NAMED_TYPE_REFERENCE)).thenReturn(JSTypeFactory.createStringType());
 namedType.setValidator(validator);
 JSType actualType = namedType.resolve(errorReporter, null);
 verify(registry, times(1)).getType(NAMED_TYPE_REFERENCE);
 verify(validator, times(1)).apply(actualType);
 assertSame(JSTypeFactory.createStringType(), actualType);
 }
}