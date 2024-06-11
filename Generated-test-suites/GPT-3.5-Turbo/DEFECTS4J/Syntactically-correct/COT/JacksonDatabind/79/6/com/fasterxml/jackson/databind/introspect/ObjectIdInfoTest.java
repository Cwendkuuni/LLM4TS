
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    
    @Test
    public void testGetPropertyName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class,
                ObjectIdGenerator.class, false, ObjectIdResolver.class);
        PropertyName propertyName = objectIdInfo.getPropertyName();
        assertEquals("name", propertyName.getSimpleName());
    }
    
    @Test
    public void testGetScope() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class,
                ObjectIdGenerator.class, false, ObjectIdResolver.class);
        Class<?> scope = objectIdInfo.getScope();
        assertEquals(String.class, scope);
    }
    
    @Test
    public void testGetGeneratorType() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class,
                ObjectIdGenerator.class, false, ObjectIdResolver.class);
        Class<? extends ObjectIdGenerator<?>> generatorType = objectIdInfo.getGeneratorType();
        assertEquals(ObjectIdGenerator.class, generatorType);
    }
    
    @Test
    public void testGetResolverType() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class,
                ObjectIdGenerator.class, false, ObjectIdResolver.class);
        Class<? extends ObjectIdResolver> resolverType = objectIdInfo.getResolverType();
        assertEquals(ObjectIdResolver.class, resolverType);
    }
    
    @Test
    public void testGetAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class,
                ObjectIdGenerator.class, false, ObjectIdResolver.class);
        boolean alwaysAsId = objectIdInfo.getAlwaysAsId();
        assertFalse(alwaysAsId);
    }
    
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class,
                ObjectIdGenerator.class, false, ObjectIdResolver.class);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class,
                ObjectIdGenerator.class, false, ObjectIdResolver.class);
        String expected = "ObjectIdInfo: propName=name, scope=java.lang.String, " +
                "generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, " +
                "alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }
}
