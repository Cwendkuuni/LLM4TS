import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance() throws Exception {
        // Given
        Class<String> stringClass = String.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        // When
        String result = constructorInstantiator.newInstance(stringClass);

        // Then
        Assert.assertNotNull(result);
    }
}