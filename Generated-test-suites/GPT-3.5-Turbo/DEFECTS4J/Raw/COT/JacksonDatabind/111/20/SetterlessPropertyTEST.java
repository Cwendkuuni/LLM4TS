
import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import java.lang.reflect.Method;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdScalarDeserializer;

public class SetterlessPropertyTest {

    private SetterlessProperty sp;
    private String testName;

    @Test
    public void testSetterlessProperty() {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        
        SetterlessProperty sp = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        Assert.assertNotNull(sp);
    }

    @Test
    public void testWithNullProvider() {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty src = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        NullValueProvider nva = new NullValueProvider();
        
        SetterlessProperty sp = new SetterlessProperty(src, nva);
        Assert.assertNotNull(sp);
    }

    @Test
    public void testWithNullProviderAndDeserializer() {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty src = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        NullValueProvider nva = new NullValueProvider();
        StdScalarDeserializer<?> deser = new StdScalarDeserializer<>();
        
        SetterlessProperty sp = new SetterlessProperty(src, deser, nva);
        Assert.assertNotNull(sp);
    }

    @Test
    public void testWithNewName() {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty src = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        PropertyName newName = new PropertyName();
        
        SetterlessProperty sp = new SetterlessProperty(src, newName);
        Assert.assertNotNull(sp);
    }

    @Test
    public void testWithValueDeserializer() {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty src = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        StdDeserializer<?> deser = new StdDeserializer<>();
        
        SetterlessProperty sp = new SetterlessProperty(src, deser);
        Assert.assertNotNull(sp);
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty sp = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        DeserializationConfig config = new DeserializationConfig();
        
        sp.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty sp = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        
        Annotation annotation = sp.getAnnotation(Annotation.class);
        Assert.assertNotNull(annotation);
    }

    @Test
    public void testGetMember() {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty sp = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        
        AnnotatedMember member = sp.getMember();
        Assert.assertNotNull(member);
    }

    @Test
    public void testDeserializeAndSetWithValueNull() throws IOException {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty sp = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        JsonParser p = new ObjectMapper().deserialize("p", JsonParser.class);
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        JsonToken t = JsonToken.VALUE_NULL;
        if (t == JsonToken.VALUE_NULL) {
            return;
        }
        
        sp.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeAndSetWithoutValueTypeDeserializer() throws IOException {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty sp = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        JsonParser p = new ObjectMapper().deserialize("p", JsonParser.class);
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        JsonToken t = JsonToken.VALUE_NULL;
        if (t == JsonToken.VALUE_NULL) {
            return;
        }
        
        Object toModify;
        try {
            toModify = getter.invoke(instance, new Object[] {null});
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        
        if (toModify == null) {
            String.format("Problem deserializing 'setterless' property '%s': get method returned null", sp.getName());
        }
        
        sp.deserializeAndSet(p, ctxt, toModify);
    }

    @Test
    public void testDeserializeAndSetWithValueTypeDeserializer() throws IOException {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty sp = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        JsonParser p = new ObjectMapper().deserialize("p", JsonParser.class);
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        JsonToken t = JsonToken.VALUE_NULL;
        if (t == JsonToken.VALUE_NULL) {
            return;
        }
        
        Object toModify;
        try {
            toModify = getter.invoke(instance, new Object[] {null});
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        
        if (toModify == null) {
            String.format("Problem deserializing 'setterless' property '%s': get method returned null", sp.getName());
        }
        
        sp.deserializeAndSet(p, ctxt, toModify);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty sp = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        JsonParser p = new ObjectMapper().deserialize("p", JsonParser.class);
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        JsonToken t = JsonToken.VALUE_NULL;
        if (t == JsonToken.VALUE_NULL) {
            return;
        }
        
        sp.deserializeAndSet(p, ctxt, instance);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = new ObjectMapper().deserialize("propDef", BeanPropertyDefinition.class);
        Method getter = new Method();
        SetterlessProperty sp = new SetterlessProperty(propDef, new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        Object instance = new Object();
        Object value = new Object();
        
        sp.set(instance, value);
    }
}
