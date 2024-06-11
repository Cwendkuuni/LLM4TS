import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class CreatorCollectorTest {
    
    private CreatorCollector creatorCollector;
    
    @Before
    public void setUp() {
        BeanDescription beanDesc = new BeanDescription(Bean.class);
        creatorCollector = new CreatorCollector(beanDesc, true);
    }
    
    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new Creator();
        creatorCollector.setDefaultCreator(creator);
        
        assertThat(creatorCollector.hasDefaultCreator(), is(true));
        assertThat(creatorCollector._creators[CreatorCollector.C_DEFAULT], is(creator));
    }
    
    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new Creator();
        creatorCollector.addStringCreator(creator, true);
        
        assertThat(creatorCollector._creators[CreatorCollector.C_STRING], is(creator));
        assertThat(creatorCollector._explicitCreators & (1 << CreatorCollector.C_STRING), is(not(0)));
    }
    
    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new Creator();
        creatorCollector.addIntCreator(creator, true);
        
        assertThat(creatorCollector._creators[CreatorCollector.C_INT], is(creator));
        assertThat(creatorCollector._explicitCreators & (1 << CreatorCollector.C_INT), is(not(0)));
    }
    
    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new Creator();
        creatorCollector.addLongCreator(creator, true);
        
        assertThat(creatorCollector._creators[CreatorCollector.C_LONG], is(creator));
        assertThat(creatorCollector._explicitCreators & (1 << CreatorCollector.C_LONG), is(not(0)));
    }
    
    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new Creator();
        creatorCollector.addDoubleCreator(creator, true);
        
        assertThat(creatorCollector._creators[CreatorCollector.C_DOUBLE], is(creator));
        assertThat(creatorCollector._explicitCreators & (1 << CreatorCollector.C_DOUBLE), is(not(0)));
    }
    
    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new Creator();
        creatorCollector.addBooleanCreator(creator, true);
        
        assertThat(creatorCollector._creators[CreatorCollector.C_BOOLEAN], is(creator));
        assertThat(creatorCollector._explicitCreators & (1 << CreatorCollector.C_BOOLEAN), is(not(0)));
    }
    
    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new Creator();
        CreatorProperty[] properties = {new CreatorProperty()};
        creatorCollector.addDelegatingCreator(creator, true, properties);
        
        assertThat(creatorCollector._creators[CreatorCollector.C_DELEGATE], is(creator));
        assertThat(creatorCollector._explicitCreators & (1 << CreatorCollector.C_DELEGATE), is(not(0)));
        assertThat(creatorCollector._delegateArgs, is(properties));
    }
    
    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new Creator();
        CreatorProperty[] properties = {new CreatorProperty()};
        creatorCollector.addPropertyCreator(creator, true, properties);
        
        assertThat(creatorCollector._creators[CreatorCollector.C_PROPS], is(creator));
        assertThat(creatorCollector._explicitCreators & (1 << CreatorCollector.C_PROPS), is(not(0)));
        assertThat(creatorCollector._propertyBasedArgs, is(properties));
    }
    
    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        
        assertThat(creatorCollector._incompleteParameter, is(parameter));
    }
    
    // Additional test cases can be added to cover all scenarios
    
}

class Bean {
    
}

class Creator implements AnnotatedWithParams {
    
}