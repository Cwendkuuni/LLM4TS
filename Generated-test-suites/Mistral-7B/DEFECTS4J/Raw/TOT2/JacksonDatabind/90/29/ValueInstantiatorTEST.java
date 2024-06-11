import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationTest;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ValueInstantiatorTest extends DeserializationTest {

    @Test
    public void testDefaultInstantiator() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(String.class);
        Object result = instantiator.createUsingDefault(context());
        assertNotNull(result);
        assertEquals("", result.toString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testCanCreateFromString_String() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(String.class);
        String json = "{\"value\": \"test\"}";
        Object result = instantiator.createFromObjectWith(context(), new Object[]{new AnnotatedClass(String.class)});
        assertEquals("test", result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testCanCreateFromString_NonString() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(Integer.class);
        String json = "{\"value\": \"test\"}";
        Object result = instantiator.createFromObjectWith(context(), new Object[]{new AnnotatedClass(String.class)});
        assertNotNull(result);
    }

    @Test
    public void testCanCreateFromInt() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(Integer.class);
        Object result = instantiator.createFromInt(context(), 10);
        assertEquals(10, result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testExceptionWhenCanCreateFromInt_NonInteger() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(String.class);
        Object result = instantiator.createFromInt(context(), "10");
        assertNotNull(result);
    }

    @Test
    public void testCanCreateFromLong() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(Long.class);
        Object result = instantiator.createFromLong(context(), 10L);
        assertEquals(10L, result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testExceptionWhenCanCreateFromLong_NonLong() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(String.class);
        Object result = instantiator.createFromLong(context(), "10L");
        assertNotNull(result);
    }

    @Test
    public void testCanCreateFromDouble() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(Double.class);
        Object result = instantiator.createFromDouble(context(), 10.0);
        assertEquals(10.0, result, 0.001);
    }

    @Test(expected = JsonProcessingException.class)
    public void testExceptionWhenCanCreateFromDouble_NonDouble() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(String.class);
        Object result = instantiator.createFromDouble(context(), "10.0");
        assertNotNull(result);
    }

    @Test
    public void testCanCreateFromBoolean() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(Boolean.class);
        Object result = instantiator.createFromBoolean(context(), true);
        assertEquals(true, result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testExceptionWhenCanCreateFromBoolean_NonBoolean() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(String.class);
        Object result = instantiator.createFromBoolean(context(), "true");
        assertNotNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testExceptionWhenCanCreateUsingDefault() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(String.class);
        Object result = instantiator.createUsingDefault(context());
        assertNotNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testExceptionWhenCanCreateUsingDelegate() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(String.class);
        Object delegate = new Object();
        Object result = instantiator.createUsingDelegate(context(), delegate);
        assertNotNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testExceptionWhenCanCreateUsingArrayDelegate() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(String.class);
        Object delegate = new Object[0];
        Object result = instantiator.createUsingArrayDelegate(context(), delegate);
        assertNotNull(result);
    }

    @Test
    public void testCanCreateFromObjectWith() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(ValueInstantiator.class);
        Object[] args = new Object[]{new AnnotatedClass(String.class), "test"};
        Object result = instantiator.createFromObjectWith(context(), args);
        assertNotNull(result);
        assertEquals("test", result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testExceptionWhenCanCreateFromObjectWith_MissingArgument() throws Exception {
        ValueInstantiator instantiator = mapper().findValueInstantiator(ValueInstantiator.class);
        Object[] args = new Object[]{new AnnotatedClass(String.class)};
        Object result = instantiator.createFromObjectWith(context(), args);
        assertNotNull(result);
    }
}