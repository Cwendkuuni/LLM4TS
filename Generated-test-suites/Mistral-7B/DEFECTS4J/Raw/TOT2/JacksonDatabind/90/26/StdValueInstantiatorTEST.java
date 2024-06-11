import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

public class StdValueInstantiatorTest {

    @Test
    public void testCreateUsingDefault() throws IOException {
        DeserializationConfig config = new DeserializationConfig();
        StdValueInstantiator instantiator = new StdValueInstantiator(config, Object.class);
        Object result = instantiator.createUsingDefault(new TestContext());
        assertNotNull(result);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        DeserializationConfig config = new DeserializationConfig();
        Object[] args = new Object[]{"arg1", "arg2"};
        StdValueInstantiator instantiator = new StdValueInstantiator();
        instantiator.configureFromObjectSettings(_withArgsCreator, null, null, null, null, args);
        Object result = instantiator.createFromObjectWith(new TestContext(), args);
        assertNotNull(result);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        DeserializationConfig config = new DeserializationConfig();
        Object delegate = new Object();
        StdValueInstantiator instantiator = new StdValueInstantiator();
        instantiator.configureFromDelegateSettings(_delegateCreator, null, null);
        Object result = instantiator.createUsingDelegate(new TestContext(), delegate);
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateUsingDelegateWithException() throws IOException {
        DeserializationConfig config = new DeserializationConfig();
        Object delegate = new Object();
        StdValueInstantiator instantiator = new StdValueInstantiator();
        instantiator.configureFromDelegateSettings(_delegateCreator, null, null);
        Object result = instantiator.createUsingDelegate(new TestContextWithException(), delegate);
        fail("Exception expected");
    }

    @Test
    public void testCreateFromString() throws IOException {
        DeserializationConfig config = new DeserializationConfig();
        StdValueInstantiator instantiator = new StdValueInstantiator();
        instantiator.configureFromStringCreator(_fromStringCreator);
        Object result = instantiator.createFromString(new TestContext(), "test");
        assertNotNull(result);
    }

    // Add test cases for other methods (createFromInt, createFromLong, createFromDouble, createFromBoolean)

    static class TestContext {
        // empty
    }

    static class TestContextWithException extends TestContext {
        // throw an exception in the constructor
        public TestContextWithException() {
            throw new JsonMappingException("expected exception");
        }
    }
}