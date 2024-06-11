import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private DeserializationConfig config;
    private JavaType stringType;
    private CreatorCollector creatorCollector;
    private ValueInstantiator valueInstantiator;
    private BeanDescription beanDesc;
    private MapperConfig mapperConfig;
    private AnnotatedWithParams constructor;
    private SettableBeanProperty[] properties;

    @Before
    public void setUp() {
        config = new DeserializationConfig();
        stringType = TypeFactory.defaultInstance().constructType(String.class);
        constructor = new AnnotatedConstructor(TestClass.class.getConstructor(String.class));
        properties = new SettableBeanProperty[1];
        properties[0] = new CreatorProperty.Builder(stringType, null, null, null, constructor, 0).build();
        AnnotatedWithParams stringCreator = new AnnotatedConstructor(TestClass.class.getConstructor(String.class));
        AnnotatedWithParams intCreator = new AnnotatedConstructor(TestClass.class.getConstructor(int.class));
        AnnotatedWithParams longCreator = new AnnotatedConstructor(TestClass.class.getConstructor(long.class));
        AnnotatedWithParams doubleCreator = new AnnotatedConstructor(TestClass.class.getConstructor(double.class));
        AnnotatedWithParams booleanCreator = new AnnotatedConstructor(TestClass.class.getConstructor(boolean.class));
        AnnotatedWithParams delegateCreator = new AnnotatedConstructor(TestClass.class.getConstructor(TestClass.class));
        AnnotatedWithParams propsCreator = new AnnotatedConstructor(TestClass.class.getConstructor(TestClass.class));
        creatorCollector = new CreatorCollector(beanDesc, mapperConfig);
        creatorCollector.addStringCreator(stringCreator, false);
        creatorCollector.addIntCreator(intCreator, false);
        creatorCollector.addLongCreator(longCreator, false);
        creatorCollector.addDoubleCreator(doubleCreator, false);
        creatorCollector.addBooleanCreator(booleanCreator, false);
        creatorCollector.addDelegatingCreator(delegateCreator, false, new SettableBeanProperty[0]);
        creatorCollector.addPropertyCreator(propsCreator, false, properties);
        creatorCollector.addIncompeteParameter(new AnnotatedParameter(constructor, null, null));
        valueInstantiator = creatorCollector.constructValueInstantiator(config);
    }

    @Test
    public void testConstructValueInstantiator() {
        assertNotNull(valueInstantiator);
    }

    @Test
    public void testHasDefaultCreator() {
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyCreatorDuplicate() {
        creatorCollector.addPropertyCreator(constructor, false, properties);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNonDupConflict() {
        AnnotatedWithParams conflictingCreator = new AnnotatedConstructor(AnotherTestClass.class.getConstructor(String.class));
        creatorCollector.verifyNonDup(conflictingCreator, 1, false);
    }

    @Test
    public void testVerifyNonDup() {
        AnnotatedWithParams newCreator = new AnnotatedConstructor(AnotherTestClass.class.getConstructor(String.class));
        creatorCollector.verifyNonDup(newCreator, 1, false);
        assertEquals(newCreator, creatorCollector._creators[1]);
    }

    @Test
    public void testConstructorCollector() {
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, mapperConfig);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testFixAccess() {
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, mapperConfig);
        AnnotatedMember member = new AnnotatedField(TestClass.class.getDeclaredField("name"), null, null);
        AnnotatedMember fixedMember = creatorCollector._fixAccess(member);
        assertNotNull(fixedMember);
    }

    @Test
    public void testVanilla() {
        CreatorCollector.Vanilla vanilla = new CreatorCollector.Vanilla(1);
        assertEquals(ArrayList.class.getName(), vanilla.getValueTypeDesc());
        assertTrue(vanilla.canInstantiate());
        assertTrue(vanilla.canCreateUsingDefault());
        assertNotNull(vanilla.createUsingDefault(null));
    }
}

class TestClass {
    private String name;

    public TestClass(String name) {
        this.name = name;
    }
}

class AnotherTestClass {
    private String name;

    public AnotherTestClass(String name) {
        this.name = name;
    }
}