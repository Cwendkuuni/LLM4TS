import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class NamedTypeTest {
  private NamedType namedType;
  private JSTypeRegistry typeRegistry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> staticScope;
  private static final String REFERENCE = "testReference";
  private static final String SOURCE_NAME = "testSourceName";
  private static final int LINE_NUMBER = 1;
  private static final int CHAR_NUMBER = 1;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    errorReporter = mock(ErrorReporter.class);
    staticScope = mock(StaticScope.class);
    namedType = spy(new NamedType(typeRegistry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER));
  }

  @Test
  public void testIsNamedType() {
    assertThat(namedType.isNamedType()).isTrue();
  }

  @Test
  public void testIsNominalType() {
    assertThat(namedType.isNominalType()).isTrue();
  }

  @Test
  public void testIsResolved() {
    assertThat(namedType.isResolved()).isFalse();
  }

  @Test
  public void testGetReferencedType() {
    when(typeRegistry.getType(REFERENCE)).thenReturn(createTypeMock());
    when(typeRegistry.isLastGeneration()).thenReturn(true);

    namedType.resolveInternal(errorReporter, staticScope);

    JSType referencedType = namedType.getReferencedType();
    assertThat(referencedType).isSameInstanceAs(createTypeMock().mock());
  }

  @Test
  public void testGetReferenceName() {
    assertThat(namedType.getReferenceName()).isEqualTo(REFERENCE);
  }

  @Test
  public void testToStringHelper() {
    assertThat(namedType.toStringHelper(false)).isEqualTo(REFERENCE);
  }

  @Test
  public void testHasReferenceName() {
    assertThat(namedType.hasReferenceName()).isTrue();
  }

  @Test
  public void testDefineProperty() {
    JSType type = createTypeMock();

    when(staticScope.getSlot(REFERENCE)).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return null;
      }

      @Override
      public void setType(JSType newValue) {
        // empty
      }
    });

    when(typeRegistry.getType(REFERENCE)).thenReturn(type);

    namedType.defineProperty("propName", type, false, null);

    StaticSlot<JSType> slot = staticScope.getSlot(REFERENCE);
    JSType slotValue = slot.getType();

    assertThat(slotValue).isSameInstanceAs(type);
  }

  @Test(expected = TypeError.class)
  public void testDefinePropertyWhileUnresolved() {
    when(staticScope.getSlot(REFERENCE)).thenReturn(null);

    namedType.defineProperty("propName", createTypeMock(), false, null);
  }

  @Test
  public void testFinishPropertyContinuations() {
    ObjectType referencedObjType = mock(ObjectType.class);

    when(typeRegistry.isLastGeneration()).thenReturn(true);
    when(referencedObjType.isUnknownType()).thenReturn(false);

    namedType.finishPropertyContinuations();

    assertThat(namedType.getReferencedObjTypeInternal()).isSameInstanceAs(referencedObjType);
  }

  @Test(expected = IllegalStateException.class)
  public void testFinishPropertyContinuationsWhileUnresolved() {
    namedType.finishPropertyContinuations();
  }

  @Test
  public void testResolveInternal() {
    when(typeRegistry.getType(REFERENCE)).thenReturn(createTypeMock());

    namedType.resolveInternal(errorReporter, staticScope);

    JSType resolvedType = namedType.getReferencedType();
    assertThat(resolvedType).isSameInstanceAs(createTypeMock().mock());
    assertThat(namedType.isResolved()).isTrue();
  }

  @Test
  public void testResolveInternalWithTypeCycle() {
    NamedType cycleType = spy(new NamedType(typeRegistry, "cycleType", SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER));
    when(typeRegistry.getType(REFERENCE)).thenReturn(cycleType);

    namedType.resolveInternal(errorReporter, staticScope);

    // No assertions here as this test should trigger a warning but not throw an exception
  }

  @Test
  public void testResolveInternalWithEnumElementCycle() {
    NamedType enumElementType = spy(new NamedType(typeRegistry, "enumElementType", SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER));
    when(typeRegistry.getType("enumType")).thenReturn(mock(EnumType.class));
    when(typeRegistry.getType(REFERENCE)).thenReturn(enumElementType);

    namedType.resolveInternal(errorReporter, staticScope);

    // No assertions here as this test should trigger a warning but not throw an exception
  }

  @Test
  public void testResolveInternalWhenForwardDeclared() {
    when(typeRegistry.isForwardDeclaredType(REFERENCE)).thenReturn(true);
    when(typeRegistry.getType(REFERENCE)).thenReturn(createTypeMock().mock());

    namedType.resolveInternal(errorReporter, staticScope);

    JSType resolvedType = namedType.getReferencedType();
    assertThat(resolvedType).isSameInstanceAs(createTypeMock().mock());
    assertThat(namedType.isResolved()).isTrue();
  }

  @Test
  public void testResolveInternalWhenUnresolved() {
    when(typeRegistry.getType(REFERENCE)).thenReturn(null);

    namedType.resolveInternal(errorReporter, staticScope);

    // No assertions here as this test should trigger a warning but not throw an exception
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = Mockito.any();

    namedType.setValidator(validator);

    namedType.setReferencedType(createTypeMock());

    namedType.validator.apply(namedType.getReferencedType());
  }

  private JSType createTypeMock() {
    return mock(JSType.class, new Answer<JSType>() {
      @Override
      public JSType answer(InvocationOnMock invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        return (JSType) args[0];
      }
    });
  }
}