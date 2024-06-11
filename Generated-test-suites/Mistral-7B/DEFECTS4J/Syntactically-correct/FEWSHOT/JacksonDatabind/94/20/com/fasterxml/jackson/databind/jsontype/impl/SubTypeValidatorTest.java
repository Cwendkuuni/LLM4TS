package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_whenClassIsInBlackList_thenThrowJsonMappingException() {
        DeserializationContext context = createMockContext();
        JavaType type = createMockJavaType("org.apache.commons.collections.functors.InvokerTransformer");

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testValidateSubType_whenClassIsNotInBlackList_thenDoNothing() {
        DeserializationContext context = createMockContext();
        JavaType type = createMockJavaType("java.lang.String");

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubType_whenClassIsInterface_thenDoNothing() {
        DeserializationContext context = createMockContext();
        JavaType type = createMockJavaType("java.util.List");

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubType_whenClassIsSpringFramework_thenCheckSuperClasses() {
        DeserializationContext context = createMockContext();
        JavaType type = createMockJavaType("org.springframework.beans.factory.config.PropertyPathFactoryBean");

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, type);

        // Need to check if the correct superclass is checked
        JavaType propertyPathFactoryBeanType = createMockJavaType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        JavaType abstractApplicationContextType = createMockJavaType("org.springframework.beans.factory.config.AbstractApplicationContext");
        JavaType objectType = createMockJavaType("java.lang.Object");

        JavaType[] superClasses = propertyPathFactoryBeanType.getSuperClasses();

        for (JavaType superClass : superClasses) {
            if (superClass.equals(abstractApplicationContextType) || superClass.equals(objectType)) {
                break;
            }

            if (validator.instance()._cfgIllegalClassNames.contains(superClass.getRawClass().getName())) {
                throw new AssertionError("Unexpected superclass: " + superClass.getRawClass().getName());
            }
        }
    }

    @Test
    public void testValidateSubType_whenClassIsSpringFramework_thenDoNotCheckIfInterface() {
        DeserializationContext context = createMockContext();
        JavaType type = createMockJavaType("org.springframework.beans.factory.config.AbstractApplicationContext");

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, type);

        // Check if the interface check is not performed
        JavaType abstractApplicationContextInterfaceType = createMockJavaType("org.springframework.beans.factory.config.AbstractApplicationContext");
        JavaType javaLangObjectType = createMockJavaType("java.lang.Object");

        assertFalse(validator.instance()._cfgIllegalClassNames.contains(abstractApplicationContextInterfaceType.getRawClass().getName()));
    }

    private DeserializationContext createMockContext() {
        return mock(DeserializationContext.class);
    }

    private JavaType createMockJavaType(String className) {
        return mock(JavaType.class, new MockitoStubber() {
            @Override
            public void callStub(MethodCall call) throws Exception {
                if (call.getMethodName().equals("getRawClass")) {
                    call.answersWith(new Answer<Class<?>>() {
                        @Override
                        public Class<?> answer(InvocationOnMock invocationOnMock) throws Throwable {
                            return Class.forName(className);
                        }
                    });
                }

                if (call.getMethodName().equals("getSuperClasses")) {
                    call.answersWith(new Answer<JavaType[]>() {
                        @Override
                        public JavaType[] answer(InvocationOnMock invocationOnMock) throws Throwable {
                            return new JavaType[0];
                        }
                    });
                }
            }
        });
    }
}