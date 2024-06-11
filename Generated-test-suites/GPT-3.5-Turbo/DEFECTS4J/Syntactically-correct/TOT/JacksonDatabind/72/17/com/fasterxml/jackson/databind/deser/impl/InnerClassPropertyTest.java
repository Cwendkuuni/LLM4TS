import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class InnerClassPropertyTest {

    private InnerClassProperty innerClassProperty;
    private SettableBeanProperty delegate;
    private Constructor<?> ctor;
    private AnnotatedConstructor annotated;

    @Before
    public void setUp() throws Exception {
        delegate = new SettableBeanProperty();
        ctor = new Constructor<?>();
        innerClassProperty = new InnerClassProperty(delegate, ctor);
        annotated = new AnnotatedConstructor();
    }

    @Test
    public void testGetDelegate() {
        assertEquals(delegate, innerClassProperty.getDelegate());
    }

    @Test
    public void testGetCreator() {
        assertEquals(ctor, innerClassProperty.getCreator());
    }

    @Test
    public void testGetAnnotated() {
        innerClassProperty.setAnnotated(annotated);
        assertEquals(annotated, innerClassProperty.getAnnotated());
    }

    @Test
    public void testWithAnnotated() {
        InnerClassProperty newInnerClassProperty = innerClassProperty.withAnnotated(annotated);
        assertEquals(annotated, newInnerClassProperty.getAnnotated());
    }

    @Test
    public void testWithDeser() {
        JsonDeserializer<?> deser = new JsonDeserializer<?>();
        InnerClassProperty newInnerClassProperty = innerClassProperty.withDeser(deser);
        assertEquals(deser, newInnerClassProperty.getDeser());
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName();
        InnerClassProperty newInnerClassProperty = innerClassProperty.withNewName(newName);
        assertEquals(newName, newInnerClassProperty.getNewName());
    }

    @Test
    public void testAssignIndex() {
        int index = 5;
        innerClassProperty.assignIndex(index);
        assertEquals(index, innerClassProperty.getIndex());
    }

    @Test
    public void testGetPropertyIndex() {
        int index = 5;
        innerClassProperty.setIndex(index);
        assertEquals(index, innerClassProperty.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        assertNull(innerClassProperty.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        assertNull(innerClassProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        
        // Test with JsonToken.VALUE_NULL
        jp.setCurrentToken(JsonToken.VALUE_NULL);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertNull(innerClassProperty.getValue());
        
        // Test with _valueTypeDeserializer != null
        innerClassProperty.setValueTypeDeserializer(new ValueTypeDeserializer());
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        // Test with normal case
        innerClassProperty.setValueTypeDeserializer(null);
        innerClassProperty.setCreator(Constructor);
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertNotNull(innerClassProperty.getValue());
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        jp.setCurrentToken(JsonToken.VALUE_NUMBER_INT);
        assertNotNull(innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance));
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        
        innerClassProperty.set(instance, value);
        assertEquals(value, innerClassProperty.getValue());
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        
        assertEquals(value, innerClassProperty.setAndReturn(instance, value));
    }

    @Test
    public void testReadResolve() {
        assertNotNull(innerClassProperty.readResolve());
    }

    @Test
    public void testWriteReplace() {
        assertNull(innerClassProperty.writeReplace());
        
        AnnotatedConstructor newAnnotated = new AnnotatedConstructor();
        innerClassProperty.setAnnotated(newAnnotated);
        
        assertNotNull(innerClassProperty.writeReplace());
    }
}