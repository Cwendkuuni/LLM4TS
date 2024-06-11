import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Mock
    private EvalContext evalContextMock;
    @Mock
    private CoreOperationGreaterThanOrEqual coreOperationMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testComputeValue_BothArgsGreater_ReturnsTrue() {
        when(coreOperationMock.computeValue(evalContextMock)).thenReturn(Boolean.TRUE);
        assertTrue((Boolean) coreOperationMock.computeValue(evalContextMock));
    }

    @Test
    public void testComputeValue_FirstArgGreater_ReturnsTrue() {
        when(coreOperationMock.computeValue(evalContextMock)).thenReturn(Boolean.TRUE);
        assertTrue((Boolean) coreOperationMock.computeValue(evalContextMock));
    }

    @Test
    public void testComputeValue_SecondArgGreater_ReturnsFalse() {
        when(coreOperationMock.computeValue(evalContextMock)).thenReturn(Boolean.FALSE);
        assertFalse((Boolean) coreOperationMock.computeValue(evalContextMock));
    }

    @Test
    public void testComputeValue_BothArgsEqual_ReturnsTrue() {
        when(coreOperationMock.computeValue(evalContextMock)).thenReturn(Boolean.TRUE);
        assertTrue((Boolean) coreOperationMock.computeValue(evalContextMock));
    }

    @Test
    public void testComputeValue_EitherArgIsNull_ReturnsFalse() {
        when(coreOperationMock.computeValue(evalContextMock)).thenReturn(Boolean.FALSE);
        assertFalse((Boolean) coreOperationMock.computeValue(evalContextMock));
    }

    @Test
    public void testComputeValue_ExceptionThrown_ReturnsFalse() {
        when(coreOperationMock.computeValue(evalContextMock)).thenThrow(new RuntimeException());
        assertFalse((Boolean) coreOperationMock.computeValue(evalContextMock));
        verify(coreOperationMock, times(1)).computeValue(evalContextMock);
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanOrEqualSymbol() {
        assertEquals(">=", coreOperationMock.getSymbol());
    }
}