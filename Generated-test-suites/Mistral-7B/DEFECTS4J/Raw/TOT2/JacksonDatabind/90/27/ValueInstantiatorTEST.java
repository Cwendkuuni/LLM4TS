import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDescriptor;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {

    private ObjectMapper objectMapper;
    private ValueInstantiator valueInstantiator;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        valueInstantiator = objectMapper.findValueInstantiator(String.class);
    }

    @Test
    public void testStringInstantiationFromString() throws IOException {
        String input = "\"Hello World\"";
        String expected = "Hello World";
        Object result = valueInstantiator.createFromString(new DeserializationContext(objectMapper.getFactory()), input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStringInstantiationFromNull() throws IOException {
        String input = null;
        valueInstantiator.createFromString(new DeserializationContext(objectMapper.getFactory()), input);
    }

    @Test(expected = IOException.class)
    public void testStringInstantiationFromEmptyString() throws IOException {
        String input = "";
        valueInstantiator.createFromString(new DeserializationContext(objectMapper.getFactory()), input);
    }

    @Test(expected = IOException.class)
    public void testStringInstantiationFromInvalidJson() throws IOException {
        String input = "{'name': 'John'}";
        valueInstantiator.createFromString(new DeserializationContext(objectMapper.getFactory()), input);
    }

    @Test
    public void testDefaultInstantiation() throws IOException {
        Class<ValueInstantiator> clazz = ValueInstantiator.class;
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(clazz);
        Object result = instantiator.createUsingDefault(new DeserializationContext(objectMapper.getFactory()));
        assertNull(result);
    }

    @Test
    public void testNullInstantiation() {
        // ValueInstantiator does not have a no-args constructor
    }

    @Test
    public void testCanCreateFromInt() throws Exception {
        Class<ValueIntFromIntInstantiator> clazz = ValueIntFromIntInstantiator.class;
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(clazz);
        DeserializationContext ctxt = new DeserializationContext(objectMapper.getFactory());
        int input = 42;
        Object result = instantiator.createFromInt(ctxt, input);
        assertEquals(input, result);
    }

    @Test
    public void testCanCreateFromLong() throws Exception {
        Class<ValueLongFromLongInstantiator> clazz = ValueLongFromLongInstantiator.class;
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(clazz);
        DeserializationContext ctxt = new DeserializationContext(objectMapper.getFactory());
        long input = 42L;
        Object result = instantiator.createFromLong(ctxt, input);
        assertEquals(input, result);
    }

    @Test
    public void testCanCreateFromDouble() throws Exception {
        Class<ValueDoubleFromDoubleInstantiator> clazz = ValueDoubleFromDoubleInstantiator.class;
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(clazz);
        DeserializationContext ctxt = new DeserializationContext(objectMapper.getFactory());
        double input = 42.0;
        Object result = instantiator.createFromDouble(ctxt, input);
        assertEquals(input, result);
    }

    @Test
    public void testCanCreateFromBoolean() throws Exception {
        Class<ValueBooleanFromBooleanInstantiator> clazz = ValueBooleanFromBooleanInstantiator.class;
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(clazz);
        DeserializationContext ctxt = new DeserializationContext(objectMapper.getFactory());
        boolean input = true;
        Object result = instantiator.createFromBoolean(ctxt, input);
        assertEquals(input, result);
    }

    @Test
    public void testCanCreateFromObjectWith() throws Exception {
        Class<ValueWithPropertiesInstantiator> clazz = ValueWithPropertiesInstantiator.class;
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(clazz);
        DeserializationContext ctxt = new DeserializationContext(objectMapper.getFactory());
        Object[] args = new Object[]{ new PropertyName("name"), "John" };
        Object result = instantiator.createFromObjectWith(ctxt, args);
        assertEquals("John", result);
    }

    @Test
    public void testCreateFromObjectWithMissingArguments() throws Exception {
        Class<ValueWithPropertiesInstantiator> clazz = ValueWithPropertiesInstantiator.class;
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(clazz);
        DeserializationContext ctxt = new DeserializationContext(objectMapper.getFactory());
        Object[] args = new Object[]{ new PropertyName("name") };
        Object result = instantiator.createFromObjectWith(ctxt, args);
        assertNull(result);
    }

    @Test
    public void testDelegateInstantiation() throws Exception {
        Class<ValueWithDelegateInstantiator> clazz = ValueWithDelegateInstantiator.class;
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(clazz);
        DeserializationContext ctxt = new DeserializationContext(objectMapper.getFactory());
        Object delegate = new Object();
        Object result = instantiator.createUsingDelegate(ctxt, delegate);
        assertEquals(delegate, result);
    }

    @Test
    public void testArrayDelegateInstantiation() throws Exception {
        Class<ValueWithArrayDelegateInstantiator> clazz = ValueWithArrayDelegateInstantiator.class;
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(clazz);
        DeserializationContext ctxt = new DeserializationContext(objectMapper.getFactory());
        Object[] arrayDelegate = new Object[1];
        Object result = instantiator.createUsingArrayDelegate(ctxt, arrayDelegate[0]);
        assertNull(result);
    }

    private static class ValueIntFromIntInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromInt() {
            return true;
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return value;
        }
    }

    private static class ValueLongFromLongInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromLong() {
            return true;
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return value;
        }
    }

    private static class ValueDoubleFromDoubleInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromDouble() {
            return true;
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return value;
        }
    }

    private static class ValueBooleanFromBooleanInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromBoolean() {
            return true;
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return value;
        }
    }

    @JsonDeserialize(using = ValueWithPropertiesDeserializer.class)
    private static class ValueWithProperties {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class ValueWithPropertiesDeserializer extends StdDeserializer<ValueWithProperties> {
        public ValueWithPropertiesDeserializer() {
            this(null);
        }

        public ValueWithPropertiesDeserializer(Class<?> vc) {
            this(vc, null);
        }

        public ValueWithPropertiesDeserializer(Class<?> vc, PropertyName pn) {
            super(vc, pn);
        }

        @Override
        public ValueWithProperties deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ValueWithProperties result = new ValueWithProperties();
            Class<?> cls = getValueClass();
            AnnotatedClass ac = AnnotatedClass.construct(cls, p, ctxt.getAnnotationIntrospector());
            AnnotationIntrospector ai = ctxt.getAnnotationIntrospector();
            Set<BeanPropertyDescriptor> props = ai.findProperties(ac, true, true, null);
            for (BeanPropertyDescriptor prop : props) {
                if (prop.getName().equals(context.getName())) {
                    Object value = context.nextValue();
                    if (value != null) {
                        prop.getMutableValue().setValueQuietly(result, value);
                    }
                    break;
                }
            }
            return result;
        }
    }

    private static class ValueWithPropertiesInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromObjectWith() {
            return true;
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            Object name = args[0];
            ValueWithProperties value = new ValueWithProperties();
            BeanPropertyDescriptor nameProp = findNameProperty(ctxt);
            nameProp.getMutableValue().setValueQuietly(value, name);
            return value;
        }

        private BeanPropertyDescriptor findNameProperty(DeserializationContext ctxt) throws IOException {
            AnnotatedClass ac = AnnotatedClass.construct(getValueClass(), ctxt.getParser(), ctxt.getAnnotationIntrospector());
            AnnotationIntrospector ai = ctxt.getAnnotationIntrospector();
            return ai.findProperty(ac, "name");
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return Collections.singleton(new SettableBeanProperty("name", null, null));
        }
    }

    private static class ValueWithDelegateInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateUsingDelegate() {
            return true;
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return delegate;
        }
    }

    private static class ValueWithArrayDelegateInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateUsingArrayDelegate() {
            return true;
        }

        @Override
        public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return delegate;
        }
    }
}