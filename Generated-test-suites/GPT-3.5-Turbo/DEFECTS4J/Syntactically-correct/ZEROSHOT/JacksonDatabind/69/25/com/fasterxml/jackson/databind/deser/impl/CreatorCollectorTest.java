import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private BeanDescription beanDesc;
    private MapperConfig<?> config;
    private DeserializationConfig deserializationConfig;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testConstructValueInstantiator() {
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(deserializationConfig);
        Assert.assertNotNull(valueInstantiator);
    }

    @Test
    public void testHasDefaultCreator() {
        boolean hasDefaultCreator = creatorCollector.hasDefaultCreator();
        Assert.assertFalse(hasDefaultCreator);
    }

    @Test
    public void testHasDelegatingCreator() {
        boolean hasDelegatingCreator = creatorCollector.hasDelegatingCreator();
        Assert.assertFalse(hasDelegatingCreator);
    }

    @Test
    public void testHasPropertyBasedCreator() {
        boolean hasPropertyBasedCreator = creatorCollector.hasPropertyBasedCreator();
        Assert.assertFalse(hasPropertyBasedCreator);
    }
}