import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_WhenFieldIsGeneric() {
        Field field = new Field();
        ParameterizedType parameterizedType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{Integer.class};
            }

            @Override
            public Type getRawType() {
                return getClass();
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        
        field.setGenericType(parameterizedType);
        
        GenericMaster genericMaster = new GenericMaster();
        Class actualType = genericMaster.getGenericType(field);
        
        assertEquals(Integer.class, actualType);
    }

    @Test
    public void testGetGenericType_WhenFieldIsNotGeneric() {
        Field field = new Field();
        field.setGenericType(null);
        
        GenericMaster genericMaster = new GenericMaster();
        Class actualType = genericMaster.getGenericType(field);
        
        assertEquals(Object.class, actualType);
    }

    @Test
    public void testGetGenericType_WhenFieldIsNestedGeneric() {
        Field field = new Field();
        ParameterizedType outerType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return new Type[]{String.class};
                    }

                    @Override
                    public Type getRawType() {
                        return getClass();
                    }

                    @Override
                    public Type getOwnerType() {
                        return null;
                    }
                }};
            }

            @Override
            public Type getRawType() {
                return getClass();
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        
        field.setGenericType(outerType);
        
        GenericMaster genericMaster = new GenericMaster();
        Class actualType = genericMaster.getGenericType(field);
        
        assertEquals(String.class, actualType);
    }

}