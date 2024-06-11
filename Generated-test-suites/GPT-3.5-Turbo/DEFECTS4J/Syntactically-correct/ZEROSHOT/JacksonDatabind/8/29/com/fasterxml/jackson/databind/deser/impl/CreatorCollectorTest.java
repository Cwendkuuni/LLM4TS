import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNull(creatorCollector._creators[0]);
        
        creatorCollector.setDefaultCreator(null);
        assertNull(creatorCollector._creators[0]);
        
        creatorCollector.setDefaultCreator(new AnnotatedWithParams() {
            @Override
            public Class<?> getDeclaringClass() {
                return null;
            }

            @Override
            public boolean hasReturnType() {
                return false;
            }

            @Override
            public Type getGenericReturnType() {
                return null;
            }

            @Override
            public AnnotatedParameter getParameter(int index) {
                return null;
            }

            @Override
            public Class<?> getRawParameterType(int index) {
                return null;
            }

            @Override
            public Type getGenericParameterType(int index) {
                return null;
            }

            @Override
            public Class<?> getRawType() {
                return null;
            }

            @Override
            public Object call() throws Exception {
                return null;
            }

            @Override
            public Object call(Object[] args) throws Exception {
                return null;
            }
        });
        assertNotNull(creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNull(creatorCollector._creators[1]);

        creatorCollector.addStringCreator(null, false);
        assertNull(creatorCollector._creators[1]);

        creatorCollector.addStringCreator(new AnnotatedWithParams() {
            @Override
            public Class<?> getDeclaringClass() {
                return null;
            }

            @Override
            public boolean hasReturnType() {
                return false;
            }

            @Override
            public Type getGenericReturnType() {
                return null;
            }

            @Override
            public AnnotatedParameter getParameter(int index) {
                return null;
            }

            @Override
            public Class<?> getRawParameterType(int index) {
                return null;
            }

            @Override
            public Type getGenericParameterType(int index) {
                return null;
            }

            @Override
            public Class<?> getRawType() {
                return null;
            }

            @Override
            public Object call() throws Exception {
                return null;
            }

            @Override
            public Object call(Object[] args) throws Exception {
                return null;
            }
        }, false);
        assertNotNull(creatorCollector._creators[1]);
    }

    //more test methods to cover other methods in the CreatorCollector class

}