import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {
    
    @Test
    public void testDefaultCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        collector.setDefaultCreator(creator);
        assertSame(creator, collector._creators[0]);
    }
    
    @Test
    public void testStringCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        collector.addStringCreator(creator, false);
        assertSame(creator, collector._creators[1]);
    }
    
    @Test
    public void testIntCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        collector.addIntCreator(creator, false);
        assertSame(creator, collector._creators[2]);
    }
    
    @Test
    public void testLongCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        collector.addLongCreator(creator, false);
        assertSame(creator, collector._creators[3]);
    }
    
    @Test
    public void testDoubleCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        collector.addDoubleCreator(creator, false);
        assertSame(creator, collector._creators[4]);
    }
    
    @Test
    public void testBooleanCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        collector.addBooleanCreator(creator, false);
        assertSame(creator, collector._creators[5]);
    }
    
    @Test
    public void testDelegatingCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        CreatorProperty[] injectables = new CreatorProperty[0];
        collector.addDelegatingCreator(creator, false, injectables);
        assertSame(creator, collector._creators[6]);
    }
    
    @Test
    public void testPropertyCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        CreatorProperty[] properties = new CreatorProperty[0];
        collector.addPropertyCreator(creator, false, properties);
        assertSame(creator, collector._creators[7]);
    }
    
    @Test
    public void testIncompeteParameter() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedParameter parameter = new AnnotatedParameter(null, null, null, 0);
        collector.addIncompeteParameter(parameter);
        assertSame(parameter, collector._incompleteParameter);
    }
}