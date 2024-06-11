import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSType;
import com.google.common.truth.Truth;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NamedTypeTest {

    @Mock
    private ErrorReporter errorReporter;

    @Mock
    private StaticScope<JSType> enclosing;

    private NamedType namedType;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        namedType = new NamedType(new JSTypeRegistry(), "testType", "TestSource", 1, 1);
    }

    @Test
    public void testResolveViaRegistry() {
        namedType.resolveInternal(errorReporter, enclosing);
        verify(enclosing).getSlot("testType");
        Truth.assertWithMessage("The referenced type should be resolved via registry")
                .that(namedType.getReferencedType())
                .isEqualTo(namedType.getReferencedObjTypeInternal());
    }

    @Test
    public void testResolveViaProperties() {
        when(enclosing.getSlot("testType")).thenReturn(mock(StaticSlot.class));
        when(enclosing.getSlot("testType").getType()).thenReturn(mock(JSType.class));
        namedType.resolveInternal(errorReporter, enclosing);
        verify(enclosing, times(2)).getSlot("testType");
        verify(enclosing).getSlot(anyString()).getType();
        Truth.assertWithMessage("The referenced type should be resolved via properties")
                .that(namedType.getReferencedType())
                .isEqualTo(namedType.getReferencedObjTypeInternal());
    }

    @Test
    public void testResolveViaProperties_withUnknownType() {
        when(enclosing.getSlot("testType")).thenReturn(mock(StaticSlot.class));
        namedType.resolveInternal(errorReporter, enclosing);
        verify(enclosing).getSlot("testType");
        verify(enclosing).getSlot(anyString()).getType();
        Truth.assertWithMessage("The referenced type should be Unknown type")
                .that(namedType.getReferencedType().isUnknownType())
                .isTrue();
    }

    @Test
    public void testHandleTypeCycle() {
        namedType.setValidator(truth -> truth.isUnknownType());
        namedType.resolveInternal(errorReporter, enclosing);
        verify(errorReporter).warning(anyString(), anyString(), anyInt(), anyInt());
        Truth.assertWithMessage("The referenced type should be Unknown type")
                .that(namedType.getReferencedType().isUnknownType())
                .isTrue();
    }

    @Test
    public void testHandleUnresolvedType() {
        namedType.setValidator(truth -> truth.isUnknownType());
        namedType.setResolvedType(mock(JSType.class));
        namedType.resolveInternal(errorReporter, enclosing);
        verify(errorReporter).warning(anyString(), anyString(), anyInt(), anyInt());
        Truth.assertWithMessage("The resolved type should be Unknown type")
                .that(namedType.isResolved() && namedType.getResolvedTypeInternal().isUnknownType())
                .isTrue();
    }

}