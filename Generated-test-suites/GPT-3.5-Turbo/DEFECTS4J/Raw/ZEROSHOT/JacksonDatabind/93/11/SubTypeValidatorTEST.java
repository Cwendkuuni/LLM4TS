import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {
    
    private DeserializationContext mockDeserializationContext;
    private JavaType mockJavaType;
    private SubTypeValidator subTypeValidator;
    
    @Before
    public void setup() {
        mockDeserializationContext = mock(DeserializationContext.class);
        mockJavaType = mock(JavaType.class);
        subTypeValidator = SubTypeValidator.instance();
    }
    
    @Test
    public void testValidateSubType() throws JsonMappingException {
        Set<String> illegalClassNames = new HashSet<String>();
        illegalClassNames.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        illegalClassNames.add("com.mchange.v2.c3p0.JndiRefForwardingDataSource");
        illegalClassNames.add("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource");
        subTypeValidator._cfgIllegalClassNames = illegalClassNames;
        
        when(mockJavaType.getRawClass()).thenReturn(Object.class);
        
        try {
            subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);
        } catch (JsonMappingException e) {
            String expectedErrorMessage = "Illegal type (java.lang.Object) to deserialize: prevented for security reasons";
            assertEquals(expectedErrorMessage, e.getMessage());
        }
        
        verify(mockJavaType, times(1)).getRawClass();
    }
}