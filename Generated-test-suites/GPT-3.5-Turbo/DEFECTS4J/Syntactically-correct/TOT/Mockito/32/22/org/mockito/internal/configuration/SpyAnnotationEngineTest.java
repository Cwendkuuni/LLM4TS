import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.Spy;

import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        // Test case: Create mock for SpyAnnotationEngine and assert that it is not null
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Object mockResult = spyAnnotationEngine.createMockFor(null, null);
        assertNotNull(mockResult);
    }

    @Test
    public void testProcess_SpyAnnotationPresent_InstanceNull() throws IllegalAccessException {
        // Test case: Process class with @Spy annotation present but instance is null

        // Setup
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        TestClass testClass = new TestClass();

        // Assert initial state
        assertNull(testClass.spyField);

        // Run method under test
        spyAnnotationEngine.process(TestClass.class, testClass);

        // Verify
        assertNotNull(testClass.spyField);
        assertNotNull(testClass.spyFieldException);
        assertEquals(MockitoException.class, testClass.spyFieldException.getClass());
        assertEquals("Cannot create a @Spy for 'spyField' field because the *instance* is missing\n" +
                             "The instance must be created *before* initMocks();\n" +
                             "Example of correct usage of @Spy:\n" +
                             "   @Spy List mock = new LinkedList();\n" +
                             "   //also, don't forget about MockitoAnnotations.initMocks();", testClass.spyFieldException.getMessage());
    }

    @Test
    public void testProcess_SpyAnnotationPresent_InstanceNotNull_MockUtilIsMock() throws IllegalAccessException {
        // Test case: Process class with @Spy annotation present, instance is not null and MockUtil.isMock() returns true

        // Setup
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        TestClass testClass = new TestClass();
        testClass.spyField = new TestSpy();

        // Run method under test
        spyAnnotationEngine.process(TestClass.class, testClass);

        // Verify
        assertNotNull(testClass.spyField);
        assertEquals(Mockito.spy(testClass.spyField), testClass.spyField);
        assertNull(testClass.spyFieldException);
    }

    @Test
    public void testProcess_SpyAnnotationPresent_InstanceNotNull_MockUtilIsNotMock() throws IllegalAccessException {
        // Test case: Process class with @Spy annotation present, instance is not null and MockUtil.isMock() returns false

        // Setup
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        TestClass testClass = new TestClass();
        testClass.spyField = new TestClass();

        // Run method under test
        spyAnnotationEngine.process(TestClass.class, testClass);

        // Verify
        assertNotNull(testClass.spyField);
        verifyZeroInteractions(testClass.spyField);
        assertNull(testClass.spyFieldException);
    }

    @Test
    public void testAssertNoAnnotations_NoUndesiredAnnotations() {
        // Test case: Assert no undesired annotations present

        // Setup
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = null;

        // Run method under test
        spyAnnotationEngine.assertNoAnnotations(null, field);
        spyAnnotationEngine.assertNoAnnotations(null, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    }

    @Test(expected = Reporter.class)
    public void testAssertNoAnnotations_UndesiredAnnotationsPresent() {
        // Test case: Assert undesired annotations present

        // Setup
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = null;

        // Run method under test
        spyAnnotationEngine.assertNoAnnotations(null, field, Mock.class);
    }

    public class TestClass {
        @Spy
        private Object spyField;

        private Exception spyFieldException;
        
        public void setSpyFieldException(Exception spyFieldException) {
            this.spyFieldException = spyFieldException;
        }
    }
    
    public class TestSpy {
        // Empty test spy class
    }
}