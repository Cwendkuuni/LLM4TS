import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterInstance() {
        Class<String> stringClass = String.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        String instance = constructorInstantiator.newInstance(stringClass);
        assertEquals("", instance);
    }

    @Test
    public <T> void testNewInstanceWithValidClass() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<String> stringClass = String.class;
        String instance = constructorInstantiator.newInstance(stringClass);
        assertEquals("", instance);
    }
}