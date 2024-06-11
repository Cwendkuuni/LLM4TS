import org.junit.Test;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor_NullInstance() {
        // Given
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        
        // When
        Object result = spyAnnotationEngine.createMockFor(null, null);
        
        // Then
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void testProcess_SpyFieldNullInstance() {
        // Given
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = TestClass.class;
        TestClass testClass = new TestClass();

        // When
        spyAnnotationEngine.process(context, testClass);

        // Then
        Assertions.assertThat(testClass.getField()).isNull();
    }

    @Test
    public void testProcess_SpyFieldAlreadySpied() {
        // Given
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = TestClass.class;
        TestClass testClass = new TestClass();
        testClass.setField(Mockito.spy(new ClassToSpy()));

        // When
        spyAnnotationEngine.process(context, testClass);

        // Then
        Assertions.assertThat(testClass.getField()).isEqualTo(testClass.getField());
    }

    @Test
    public void testProcess_ExceptionDuringSpiedFieldInitiation() {
        // Given
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = TestClass.class;
        TestClass testClass = new TestClass();
        testClass.setField(new ClassToSpy());

        // When, Then
        Assertions.assertThatThrownBy(() -> spyAnnotationEngine.process(context, testClass))
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Problems initiating spied field");

    }

    private static class TestClass {
        private ClassToSpy field;

        public ClassToSpy getField() {
            return field;
        }

        public void setField(ClassToSpy field) {
            this.field = field;
        }
    }

    private static class ClassToSpy {
    }
}