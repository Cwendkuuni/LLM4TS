import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testConstructor() {
        BeanDescription beanDesc = new BasicBeanDescription();
        MapperConfig config = new MapperConfig();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        
        assertNotNull(creatorCollector);
        assertEquals(beanDesc, creatorCollector._beanDesc);
        assertEquals(config.canOverrideAccessModifiers(), creatorCollector._canFixAccess);
        assertEquals(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS), creatorCollector._forceAccess);
    }

    @Test
    public void testConstructValueInstantiator() {
        DeserializationConfig config = new DeserializationConfig();
        CreatorCollector creatorCollector = new CreatorCollector(new BasicBeanDescription(), new MapperConfig());
        
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        
        assertNotNull(valueInstantiator);
    }

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(new BasicBeanDescription(), new MapperConfig());
        AnnotatedWithParams creator = new AnnotatedWithParams();
        
        creatorCollector.setDefaultCreator(creator);
        
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }
}