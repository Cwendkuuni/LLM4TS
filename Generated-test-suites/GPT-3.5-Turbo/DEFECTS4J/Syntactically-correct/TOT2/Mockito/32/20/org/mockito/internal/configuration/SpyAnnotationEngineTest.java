import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.junit.Assert;

public class SpyAnnotationEngineTest {

    SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setup() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor() {
        Object mock = spyAnnotationEngine.createMockFor(null, null);
        Assert.assertNull(mock);
    }

    @Test
    public void testProcessWithNullInstance() {
        Class testClass = SpyAnnotationEngineTest.class;
        try {
            spyAnnotationEngine.process(testClass, new Object());
        } catch (MockitoException e) {
            Assert.assertEquals("Cannot create a @Spy for 'null' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcessWithExistingMock() {
        Object mock = mock(Object.class);
        Field field = mock.getClass().getDeclaredFields()[0];
        try {
            spyAnnotationEngine.process(Object.class, mock);
            Assert.fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            Assert.assertEquals("Problems initiating spied field " + field.getName(), e.getMessage());
        }
    }

    @Test
    public void testAssertNoAnnotations() {
        Field field = mock(Object.class).getClass().getDeclaredFields()[0];
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, Reporter.class);
            Assert.fail("Expected Reporter to be thrown");
        } catch (Reporter e) {
            // Expected behavior
        }
    }

}