import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.arguments.Arguments;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wantedObject = new Object();
        Same sameInstance = new Same(wantedObject);

        Object actualObject = wantedObject;

        Assert.assertTrue(sameInstance.matches(actualObject));
    }

    @Test
    public void testNullWanted() {
        Same sameInstance = new Same(null);

        Object actualObject = new Object();

        Assert.assertFalse(sameInstance.matches(actualObject));
    }

    @Test
    public void testNullActual() {
        Object wantedObject = new Object();
        Same sameInstance = new Same(wantedObject);

        Assert.assertFalse(sameInstance.matches(null));
    }

    @Test
    public void testDifferentInstances() {
        Object wantedObject1 = new Object();
        Object wantedObject2 = new Object();
        Same sameInstance = new Same(wantedObject1);

        Assert.assertFalse(sameInstance.matches(wantedObject2));
    }

    @Test
    public void testDifferentTypes() {
        Integer wantedInteger = 1;
        Same sameInstance = new Same(wantedInteger);

        String actualString = "One";

        Assert.assertFalse(sameInstance.matches(actualString));
    }

    @Test
    public void testString() {
        String wantedString = "Hello World";
        Same sameInstance = new Same(wantedString);

        String actualString = "Hello World";

        Assert.assertTrue(sameInstance.matches(actualString));
    }

    @Test
    public void testCharacter() {
        Character wantedCharacter = 'A';
        Same sameInstance = new Same(wantedCharacter);

        Character actualCharacter = 'A';

        Assert.assertTrue(sameInstance.matches(actualCharacter));
    }

    @Test
    public void testMockitoUsage() {
        Object wantedObject = Mockito.mock(Object.class);
        Same sameInstance = new Same(wantedObject);

        Object actualObject = Mockito.mock(Object.class);
        Mockito.when(actualObject.toString()).thenReturn("Mocked Object");

        Mockito.verify(sameInstance).matches(actualObject);
        Assert.assertTrue(Mockito.verifyNoMoreInteractions(sameInstance));
    }
}