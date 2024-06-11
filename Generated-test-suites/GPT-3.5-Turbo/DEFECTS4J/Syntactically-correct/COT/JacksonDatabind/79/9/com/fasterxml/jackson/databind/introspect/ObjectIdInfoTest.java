
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdGenerator;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.introspect.ObjectIdResolver;
import com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructor1() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);
        
        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructor2() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen);
        
        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }
    
    @Test
    public void testConstructor3() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("test", String.class, ObjectIdGenerator.class);
        
        assertEquals(new PropertyName("test"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, ObjectIdGenerator.class);
        
        assertEquals(objectIdInfo, objectIdInfo.withAlwaysAsId(false));
        assertEquals(true, objectIdInfo.withAlwaysAsId(true).getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, ObjectIdGenerator.class);
        
        assertEquals("ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.fasterxml.jackson.databind.introspect.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }
}
