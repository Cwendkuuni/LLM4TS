import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Test for {@link CreatorCollector} class.
 */
public class CreatorCollectorTest {

    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        MapperFactory factory = new MapperFactory();
        mapper = factory.getMapper();
    }

    // Test for default creator
    @Test
    public void testDefaultCreator() throws IOException {
        CreatorCollector collector = new CreatorCollector(new AnnotatedClass(ArrayList.class), mapper.getConfig());
        collector.setDefaultCreator(createConstructorAnnotated(ArrayList.class));
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        assertEquals(Collections.emptyList(), instantiator.createValueInstance(null));
    }

    // Test for string creator
    @Test
    public void testStringCreator() throws IOException {
        CreatorCollector collector = new CreatorCollector(new AnnotatedClass(ArrayList.class), mapper.getConfig());
        String value = "test";
        collector.addStringCreator(createConstructorAnnotated(ArrayList.class, "test"), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        List list = (List) instantiator.createValueInstance(value);
        assertEquals(value, list.toString());
    }

    // Test for int creator
    @Test
    public void testIntCreator() throws IOException {
        CreatorCollector collector = new CreatorCollector(new AnnotatedClass(ArrayList.class), mapper.getConfig());
        int value = 5;
        collector.addIntCreator(createConstructorAnnotated(ArrayList.class, int.class, value), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        List list = (List) instantiator.createValueInstance(value);
        assertEquals(value, list.size());
    }

    // Test for long creator
    @Test
    public void testLongCreator() throws IOException {
        CreatorCollector collector = new CreatorCollector(new AnnotatedClass(ArrayList.class), mapper.getConfig());
        long value = 10L;
        collector.addLongCreator(createConstructorAnnotated(ArrayList.class, long.class, value), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        List list = (List) instantiator.createValueInstance(value);
        assertEquals(value, list.size());
    }

    // Test for double creator
    @Test
    public void testDoubleCreator() throws IOException {
        CreatorCollector collector = new CreatorCollector(new AnnotatedClass(ArrayList.class), mapper.getConfig());
        double value = 3.14;
        collector.addDoubleCreator(createConstructorAnnotated(ArrayList.class, double.class, value), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        List list = (List) instantiator.createValueInstance(value);
        assertEquals(value, list.size(), 0.001);
    }

    // Test for boolean creator
    @Test
    public void testBooleanCreator() throws IOException {
        CreatorCollector collector = new CreatorCollector(new AnnotatedClass(ArrayList.class), mapper.getConfig());
        boolean value = true;
        collector.addBooleanCreator(createConstructorAnnotated(ArrayList.class, boolean.class, value), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        List list = (List) instantiator.createValueInstance(value);
        assertTrue(list instanceof ArrayList);
    }

    // Test for delegating creator
    @Test
    public void testDelegatingCreator() throws IOException {
        CreatorCollector collector = new CreatorCollector(new AnnotatedClass(MyList.class), mapper.getConfig());
        SettableBeanProperty property = new SettableBeanProperty("delegate", String.class);
        collector.addDelegatingCreator(createConstructorAnnotated(MyList.class, String[].class), false, new SettableBeanProperty[]{property});
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        String[] args = new String[]{"test1", "test2"};
        List list = (List) instantiator.createValueInstance(args);
        assertEquals(args.length, list.size());
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], list.get(i));
        }
    }

    // Test for property-based creator
    @Test
    public void testPropertyBasedCreator() throws IOException {
        CreatorCollector collector = new CreatorCollector(new AnnotatedClass(MyClass.class), mapper.getConfig());
        SettableBeanProperty property = new SettableBeanProperty("property", String.class);
        collector.addPropertyCreator(createConstructorAnnotated(MyClass.class), false, new SettableBeanProperty[]{property});
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        String value = "test";
        MapperConfig config = mapper.getConfig();
        ObjectReader objectReader = config.readerForUpdating(MyClass.class);
        MyClass obj = objectReader.readValue(new StringReader("{\"property\":\"" + value + "\"}"));
        assertEquals(value, obj.getProperty());
    }

    // Test for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws IOException {
        CreatorCollector collector = new CreatorCollector(new AnnotatedClass(ArrayList.class), mapper.getConfig());
        AnnotatedConstructor constructor = createConstructorAnnotated(ArrayList.class);
        collector.setDefaultCreator(constructor);
        collector.addStringCreator(constructor, false); // should not be allowed
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
    }

    // Test for null argument
    @Test(expected = IllegalArgumentException.class)
    public void testNullArgument() throws IOException {
        CreatorCollector collector = new CreatorCollector(new AnnotatedClass(ArrayList.class), mapper.getConfig());
        collector.addStringCreator(createConstructorAnnotated(ArrayList.class, String.class, null), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
    }

    // Test for invalid type
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidType() throws IOException {
        CreatorCollector collector = new CreatorCollector(new AnnotatedClass(ArrayList.class), mapper.getConfig());
        collector.addStringCreator(createConstructorAnnotated(ArrayList.class, Integer.class), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
    }

    private AnnotatedConstructor createConstructorAnnotated(Class<?> clazz, Class<?>... constructorArgs) throws NoSuchMethodException {
        Constructor<Object> constructor = clazz.getDeclaredConstructor(constructorArgs);
        constructor.setAccessible(true);
        return mapper.getDeserializationConfig().getAnnotationIntrospector().findConstructor(clazz, constructor);
    }

    private AnnotatedConstructor createConstructorAnnotated(Class<?> clazz) throws NoSuchMethodException {
        Constructor<Object> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return mapper.getDeserializationConfig().getAnnotationIntrospector().findConstructor(clazz, constructor);
    }

    private AnnotatedWithParams createConstructorAnnotated(Class<?> clazz, Class<?>... constructorArgs) throws NoSuchMethodException {
        return createConstructorAnnotated(clazz, constructorArgs)[0];
    }

    private AnnotatedWithParams[] createConstructorAnnotated(Class<?> clazz, Class<?>... constructorArgs) throws NoSuchMethodException {
        return mapper.getDeserializationConfig().getAnnotationIntrospector().findValueDeserializers(clazz, constructorArgs);
    }

}

class MyClass {
    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}

class MyList extends ArrayList {
    private static final long serialVersionUID = 1L;

    public MyList(String[] args) {
        super(args);
    }
}