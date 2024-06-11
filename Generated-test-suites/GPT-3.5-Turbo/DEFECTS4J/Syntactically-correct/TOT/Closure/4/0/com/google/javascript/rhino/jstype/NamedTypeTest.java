import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class NamedTypeTest {

    private JSTypeRegistry mockRegistry;
    private ErrorReporter mockErrorReporter;
    private NamedType namedType;

    @Before
    public void setUp() {
        mockRegistry = mock(JSTypeRegistry.class);
        mockErrorReporter = mock(ErrorReporter.class);
        namedType = new NamedType(mockRegistry, "reference", "sourceName", 1, 1);
    }

    @Test
    public void testResolveInternal_typeResolved_success() {
        JSType type = mock(JSType.class);
        when(mockRegistry.getType("reference")).thenReturn(type);

        namedType.resolveInternal(mockErrorReporter, null);

        verify(namedType).setReferencedAndResolvedType(eq(type), eq(mockErrorReporter), isNull());
        verify(namedType, never()).setResolvedTypeInternal(any(JSType.class));
        verify(namedType, never()).finishPropertyContinuations();
    }

    @Test
    public void testResolveInternal_referenceNotFound_success() {
        when(mockRegistry.getType("reference")).thenReturn(null);

        namedType.resolveInternal(mockErrorReporter, null);

        verify(namedType, never()).setReferencedAndResolvedType(any(JSType.class), any(ErrorReporter.class), any());
        verify(namedType, never()).setResolvedTypeInternal(any(JSType.class));
        verify(namedType, never()).finishPropertyContinuations();
        verify(mockErrorReporter, never()).warning(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testResolveInternal_referenceNotFound_handleUnresolvedType() {
        when(mockRegistry.getType("reference")).thenReturn(null);
        when(mockRegistry.isLastGeneration()).thenReturn(true);
        when(mockRegistry.isForwardDeclaredType("reference")).thenReturn(false);

        namedType.resolveInternal(mockErrorReporter, null);

        verify(namedType).handleUnresolvedType(eq(mockErrorReporter), eq(true));
        verify(namedType).setResolvedTypeInternal(any(JSType.class));
    }

    @Test
    public void testResolveInternal_typeCycle_detectedAndHandled() {
        JSType type = mock(JSType.class);
        when(mockRegistry.getType("reference")).thenReturn(type);
        namedType.setReferencedType(type);
        when(namedType.detectImplicitPrototypeCycle()).thenReturn(true);

        namedType.resolveInternal(mockErrorReporter, null);

        verify(namedType).handleTypeCycle(eq(mockErrorReporter));
        verify(namedType).setResolvedTypeInternal(any(JSType.class));
        verify(namedType, never()).finishPropertyContinuations();
    }

    @Test
    public void testResolveInternal_typeResolved_resolveViaProperties_success() {
        JSType type = mock(JSType.class);
        when(mockRegistry.getType("reference")).thenReturn(null);
        when(namedType.lookupViaProperties(mockErrorReporter, null)).thenReturn(type);

        namedType.resolveInternal(mockErrorReporter, null);

        verify(namedType).handleUnresolvedType(any(ErrorReporter.class), eq(false));
        verify(namedType, never()).handleTypeCycle(any(ErrorReporter.class));
    }

    @Test
    public void testResolveInternal_typeResolved_resolveViaProperties_nullType_returnNull() {
        when(mockRegistry.getType("reference")).thenReturn(null);
        when(namedType.lookupViaProperties(mockErrorReporter, null)).thenReturn(null);

        namedType.resolveInternal(mockErrorReporter, null);

        verify(namedType).handleUnresolvedType(any(ErrorReporter.class), eq(true));
        verify(namedType, never()).handleTypeCycle(any(ErrorReporter.class));
    }
}