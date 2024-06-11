import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Constructor;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanAsArrayBuilderDeserializer;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

public class InnerClassPropertyTest {

    private InnerClassProperty innerClassProperty;

    @Before
    public void setUp() {
        SettableBeanProperty delegate = new SettableBeanPropertyTest();
        Constructor<?> ctor = SettableBeanPropertyTest.class.getConstructor(int.class);
        innerClassProperty = new InnerClassProperty(delegate, ctor);
    }

    @Test
    public void testConstructor() throws NoSuchFieldException, IllegalAccessException {
        assertEquals(settableBeanProperty, innerClassProperty._delegate);
        assertEquals(SettableBeanPropertyTest.class.getConstructor(int.class), innerClassProperty._creator);
    }

    @Test
    public void testWithAnnotation() {
        assertEquals(null, innerClassProperty.getAnnotation(Override.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(settableBeanProperty, innerClassProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = new ObjectMapper().createParser("{\"value\":10}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        innerClassProperty.deserializeAndSet(jp, ctxt, instance);
        assertEquals(10, instance.getValue());
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        JsonParser jp = new ObjectMapper().createParser("{\"value\":null}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        innerClassProperty.deserializeAndSet(jp, ctxt, instance);
        assertEquals(0, instance.getValue());
    }

    @Test
    public void testDeserializeAndSet_TypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
        JsonParser jp = mapper.createParser("{\"value\":20}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        innerClassProperty.deserializeAndSet(jp, ctxt, instance);
        assertEquals(20, instance.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSet_Exception() throws IOException {
        JsonParser jp = new ObjectMapper().createParser("{\"value\":30}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        innerClassProperty.deserializeAndSet(jp, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = new ObjectMapper().createParser("{\"value\":40}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        Object returnedValue = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        assertEquals(40, instance.getValue());
        assertEquals(instance, returnedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeSetAndReturn_Exception() throws IOException {
        JsonParser jp = new ObjectMapper().createParser("{\"value\":50}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        innerClassProperty.set(instance, 60);
        assertEquals(60, instance.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_Exception() throws IOException {
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        innerClassProperty.set(instance, "abc");
    }

    @Test
    public void testSetAndReturn() throws IOException {
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        Object returnedValue = innerClassProperty.setAndReturn(instance, 70);
        assertEquals(70, instance.getValue());
        assertEquals(instance, returnedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAndReturn_Exception() throws IOException {
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        innerClassProperty.setAndReturn(instance, "def");
    }

    @Test
    public void testReadResolve() {
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        InnerClassPropertyTest resolvedInstance = (InnerClassPropertyTest) innerClassProperty.readResolve();
        assertEquals(instance, resolvedInstance);
    }

    @Test
    public void testWriteReplace() {
        InnerClassPropertyTest instance = new InnerClassPropertyTest();
        InnerClassPropertyTest replacedInstance = (InnerClassPropertyTest) innerClassProperty.writeReplace();
        assertEquals(instance, replacedInstance);
    }

}

class SettableBeanPropertyTest extends SettableBeanProperty {
    private static final long serialVersionUID = 1L;
    public int value;
    public SettableBeanPropertyTest() {
        super((BeanPropertyMap)null);
    }
    public SettableBeanPropertyTest(int value) {
        super((BeanPropertyMap)null);
        this.value = value;
    }
    public void set(Object instance, Object value) throws IOException {
        this.value = (int)value;
    }
    public Object setAndReturn(Object instance, Object value) throws IOException {
        set(instance, value);
        return instance;
    }
    public int getValue() {
        return value;
    }
}