
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() {
        CreatorCollector testObject = new CreatorCollector();
        assertNotNull(testObject.constructValueInstantiator());
    }

    @Test
    public void testStringCreator() {
        CreatorCollector testObject = new CreatorCollector();
        assertNotNull(testObject.constructValueInstantiator());
    }

    @Test
    public void testIntCreator() {
        CreatorCollector testObject = new CreatorCollector();
        assertNotNull(testObject.constructValueInstantiator());
    }

    @Test
    public void testLongCreator() {
        CreatorCollector testObject = new CreatorCollector();
        assertNotNull(testObject.constructValueInstantiator());
    }

    @Test
    public void testDoubleCreator() {
        CreatorCollector testObject = new CreatorCollector();
        assertNotNull(testObject.constructValueInstantiator());
    }

    @Test
    public void testBooleanCreator() {
        CreatorCollector testObject = new CreatorCollector();
        assertNotNull(testObject.constructValueInstantiator());
    }

    @Test
    public void testDelegatingCreator() {
        CreatorCollector testObject = new CreatorCollector();
        assertNotNull(testObject.constructValueInstantiator());
    }

    @Test
    public void testPropertyCreator() {
        CreatorCollector testObject = new CreatorCollector();
        assertNotNull(testObject.constructValueInstantiator());
    }

    @Test
    public void testIncompeteParameter() {
        CreatorCollector testObject = new CreatorCollector();
        assertNotNull(testObject.constructValueInstantiator());
    }
}

