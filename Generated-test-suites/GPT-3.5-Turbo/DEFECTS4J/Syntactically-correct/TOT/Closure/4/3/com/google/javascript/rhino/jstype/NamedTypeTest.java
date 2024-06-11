import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class NamedTypeTest {

  private NamedType namedType;

  @Before
  public void setup() {
    namedType = new NamedType(new JSTypeRegistry(), "test.Reference", "Test.java", 10, 5);
  }

  @Test
  public void getReferencedType_WhenTypeResolved_ReturnsType() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    // Act
    namedType.setReferencedType(expectedType);
    JSType actualType = namedType.getReferencedType();

    // Assert
    assertEquals(expectedType, actualType);
  }

  @Test
  public void getReferenceName_WhenNameExists_ReturnsName() {
    // Arrange
    String expectedName = "test.Reference";

    // Act
    String actualName = namedType.getReferenceName();

    // Assert
    assertEquals(expectedName, actualName);
  }

  @Test
  public void isNamedType_WhenCalled_ReturnsTrue() {
    // Act
    boolean isNamedType = namedType.isNamedType();

    // Assert
    assertTrue(isNamedType);
  }

  @Test
  public void isNominalType_WhenCalled_ReturnsTrue() {
    // Act
    boolean isNominalType = namedType.isNominalType();

    // Assert
    assertTrue(isNominalType);
  }

  @Test
  public void resolveInternal_WhenResolvedViaRegistry_UpdatesResolvedType() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    ErrorReporterStub errorReporter = new ErrorReporterStub();

    // Act
    boolean isResolved = namedType.resolveInternal(errorReporter, new StaticScope<>());
    JSType actualType = namedType.getResolvedTypeInternal();

    // Assert
    assertTrue(isResolved);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void resolveInternal_WhenResolvedViaProperties_UpdatesResolvedType() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionType functionType = new FunctionType(registry);
    ObjectType objectType = new ObjectType(registry);
    objectType.defineDeclaredProperty("propertyName", functionType, false, null);
    registry.setType("test", objectType);
    JSType expectedType = functionType.getInstanceType();
    namedType = new NamedType(registry, "test.propertyName", "Test.java", 10, 5);
    ErrorReporterStub errorReporter = new ErrorReporterStub();

    // Act
    boolean isResolved = namedType.resolveInternal(errorReporter, new StaticScope<>());
    JSType actualType = namedType.getResolvedTypeInternal();

    // Assert
    assertTrue(isResolved);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void resolveInternal_WhenForwardDeclared_ReturnsUnresolvedType() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporterStub errorReporter = new ErrorReporterStub();

    // Act
    boolean isResolved = namedType.resolveInternal(errorReporter, new StaticScope<>());
    JSType actualType = namedType.getResolvedTypeInternal();

    // Assert
    assertFalse(isResolved);
    assertTrue(actualType.isUnknownType());
  }

  @Test
  public void setValidator_WhenTypeResolved_CallsValidator() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType type = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    namedType.setReferencedType(type);
    namedType.setResolvedTypeInternal(type);
    Predicate<JSType> validator = t -> t.isObjectType();

    // Act
    boolean isSet = namedType.setValidator(validator);
    boolean isValidated = namedType.getResolvedTypeInternal().isValidated();

    // Assert
    assertTrue(isSet);
    assertTrue(isValidated);
  }

  private static class ErrorReporterStub implements ErrorReporter {
    private List<String> warnings;

    @Override
    public void warning(String message, String sourceName, int line, int offset) {
      warnings.add(message);
    }
    // Implement other methods of ErrorReporter if needed
  }

}