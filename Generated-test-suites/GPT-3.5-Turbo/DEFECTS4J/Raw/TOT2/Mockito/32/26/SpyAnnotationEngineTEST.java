import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.exceptions.Reporter;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor_NullAnnotationAndField_ReturnsNull() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object mock = engine.createMockFor(null, null);
        assertNull(mock);
    }

    @Test
    public void testProcess_NoSpyAnnotationPresent_DoesNothing() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(SpyAnnotationEngine.class, new Object());
        // No Spy annotation present, so no action should be taken
    }

    @Test
    public void testProcess_SpyAnnotationPresent_InstanceIsNull_ThrowsException() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        try {
            engine.process(SpyAnnotationEngine.class, new Object());
            fail("Should throw MockitoException");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'null' field because the *instance* is missing\n" +
                         "The instance must be created *before* initMocks();\n" +
                         "Example of correct usage of @Spy:\n" +
                         "   @Spy List mock = new LinkedList();\n" +
                         "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_SpyAnnotationPresent_InstanceIsMock_ResetInstance() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object mockInstance = Mockito.mock(Object.class);
        try {
            engine.process(SpyAnnotationEngine.class, mockInstance);
        } catch (MockitoException e) {
            fail("Should not throw MockitoException");
        }
        Mockito.verify(mockInstance).reset();
    }
    
    @Test
    public void testProcess_SpyAnnotationPresent_InstanceNotMock_CreateSpyInstance() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object spyInstance = new Object();
        try {
            engine.process(SpyAnnotationEngine.class, spyInstance);
        } catch (MockitoException e) {
            fail("Should not throw MockitoException");
        }
        assertTrue(Mockito.mockingDetails(spyInstance).isSpy());
    }

    @Test
    public void testAssertNoAnnotations_DesiredAnnotationsPresent_ThrowsReporterException() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        try {
            engine.assertNoAnnotations(Spy.class, null, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
            fail("Should throw ReporterException");
        } catch (Reporter e) {
            assertEquals("unsupportedCombinationOfAnnotations", e.getMessage());
        }
    }
}