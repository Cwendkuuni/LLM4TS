import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InnerClassPropertyTest {

    private InnerClassProperty innerClassProperty;
    private SettableBeanProperty delegate;
    private Constructor<?> ctor;

    @Before
    public void setUp() {
        delegate = new SettableBeanProperty();
        ctor = new Constructor<>();
        innerClassProperty = new InnerClassProperty(delegate, ctor);
    }

    @Test
    public void testConstructor() {
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }

    @Test
    public void testWithDeser() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty result = innerClassProperty.withValueDeserializer(deser);
        assertEquals(innerClassProperty, result._delegate);
        assertEquals(deser, result._delegate.getValueDeserializer());
        assertEquals(ctor, result._creator);
        assertEquals(innerClassProperty._annotated, result._annotated);
        assertEquals(innerClassProperty._valueTypeDeserializer, result._valueTypeDeserializer);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName();
        InnerClassProperty result = innerClassProperty.withName(newName);
        assertEquals(innerClassProperty, result._delegate);
        assertEquals(newName, result._delegate.getName());
        assertEquals(ctor, result._creator);
        assertEquals(innerClassProperty._annotated, result._annotated);
        assertEquals(innerClassProperty._valueTypeDeserializer, result._valueTypeDeserializer);
    }

    @Test
    public void testAssignIndex() {
        int index = 1;
        innerClassProperty.assignIndex(index);
        assertEquals(index, innerClassProperty._delegate.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndex() {
        int index = 1;
        innerClassProperty.assignIndex(index);
        assertEquals(index, innerClassProperty.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;
        assertNull(innerClassProperty.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        assertNull(innerClassProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        jp.setCurrentToken(JsonToken.VALUE_NULL);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertNull(innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeAndSet_WithValueTypeDeserializer() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        JsonDeserializer<?> valueTypeDeserializer = new JsonDeserializer<>();
        innerClassProperty._valueTypeDeserializer = valueTypeDeserializer;
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertNull(innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeAndSet_DefaultCase() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        JsonDeserializer<?> valueDeserializer = new JsonDeserializer<>();
        innerClassProperty._valueDeserializer = valueDeserializer;
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertNull(innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object expected = new Object();
        innerClassProperty._valueDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return expected;
            }
        };
        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        assertEquals(expected, result);
        assertEquals(expected, innerClassProperty.get(instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        innerClassProperty.set(instance, value);
        assertEquals(value, innerClassProperty.get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = innerClassProperty.setAndReturn(instance, value);
        assertEquals(value, innerClassProperty.get(instance));
        assertEquals(value, result);
    }

    @Test
    public void testReadResolve() {
        InnerClassProperty result = (InnerClassProperty) innerClassProperty.readResolve();
        assertEquals(innerClassProperty, result);
        assertEquals(innerClassProperty._annotated, result._annotated);
        assertEquals(innerClassProperty._creator, result._creator);
    }

    @Test
    public void testWriteReplace_WithAnnotated() {
        InnerClassProperty property = new InnerClassProperty(innerClassProperty, new AnnotatedConstructor());
        Object result = property.writeReplace();
        assertEquals(property, result);
    }

    @Test
    public void testWriteReplace_WithoutAnnotated() {
        Object result = innerClassProperty.writeReplace();
        assertEquals(result.getClass(), InnerClassProperty.class);
    }
}