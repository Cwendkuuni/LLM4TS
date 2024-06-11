import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    
    @Test
    public void testSetDefaultCreator() {
    	AnnotatedWithParams creator = new TestAnnotatedWithParams();
    	creatorCollector.setDefaultCreator(creator);
    	AnnotatedWithParamsCreatorWithParams[] creators = creatorCollector._creators;
    	assertEquals(creator, creators[0]);
    }
    
    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        boolean explicit = true;
        creatorCollector.addStringCreator(creator, explicit);
        AnnotatedWithParamsCreatorWithParams[] creators = creatorCollector._creators;
        assertEquals(creator, creators[1]);
    }
    
    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        boolean explicit = true;
        creatorCollector.addIntCreator(creator, explicit);
        AnnotatedWithParamsCreatorWithParams[] creators = creatorCollector._creators;
        assertEquals(creator, creators[2]);
    }
    
    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        boolean explicit = true;
        creatorCollector.addLongCreator(creator, explicit);
        AnnotatedWithParamsCreatorWithParams[] creators = creatorCollector._creators;
        assertEquals(creator, creators[3]);
    }
    
    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        boolean explicit = true;
        creatorCollector.addDoubleCreator(creator, explicit);
        AnnotatedWithParamsCreatorWithParams[] creators = creatorCollector._creators;
        assertEquals(creator, creators[4]);
    }
    
    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        boolean explicit = true;
        creatorCollector.addBooleanCreator(creator, explicit);
        AnnotatedWithParamsCreatorWithParams[] creators = creatorCollector._creators;
        assertEquals(creator, creators[5]);
    }
    
    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        boolean explicit = true;
        CreatorProperty[] injectables = new CreatorProperty[1];
        creatorCollector.addDelegatingCreator(creator, explicit, injectables);
        AnnotatedWithParamsCreatorWithParams[] creators = creatorCollector._creators;
        assertEquals(creator, creators[6]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }
    
    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        boolean explicit = true;
        CreatorProperty[] properties = new CreatorProperty[1];
        creatorCollector.addPropertyCreator(creator, explicit, properties);
        AnnotatedWithParamsCreatorWithParams[] creators = creatorCollector._creators;
        assertEquals(creator, creators[7]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new TestAnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    private class TestAnnotatedWithParams extends AnnotatedWithParams {
    	
    	public TestAnnotatedWithParams() {
    		super(null, null, null);
    	}

		@Override
		public AnnotatedParameter getParameter(int index) {
			return null;
		}

		@Override
		public int getParameterCount() {
			return 0;
		}

		@Override
		public Class<?> getRawType() {
			return null;
		}

		@Override
		public JavaType getType() {
			return null;
		}

		@Override
		public Iterable<AnnotatedParameter> parameters() {
			return null;
		}
    }
 
    private class TestAnnotatedParameter extends AnnotatedParameter {

        public TestAnnotatedParameter() {
            super(null, null);
        }
        
        @Override
        public AnnotatedWithParams getOwner() {
            return null;
        }

        @Override
        public int getIndex() {
            return 0;
        }

        @Override
        public Class<?> getRawType() {
            return null;
        }

        @Override
        public JavaType getType() {
            return null;
        }
    }
}