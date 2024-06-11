package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithValidClasses() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StringDeserializer());
        mapper.registerModule(module);

        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with valid classes
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        validator.validateSubType(mapper.getDeserializationContext(), stringType);

        JavaType intType = mapper.getTypeFactory().constructType(int.class);
        validator.validateSubType(mapper.getDeserializationContext(), intType);

        JavaType javaClassType = mapper.getTypeFactory().constructType(JavaType.class);
        validator.validateSubType(mapper.getDeserializationContext(), javaClassType);
    }

    @Test
    public void testValidateSubTypeWithSpringClasses() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StringDeserializer());
        mapper.registerModule(module);

        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with Spring classes
        JavaType abstractApplicationContextType = mapper.getTypeFactory().constructType("org.springframework.beans.factory.AbstractApplicationContext");
        validator.validateSubType(mapper.getDeserializationContext(), abstractApplicationContextType);

        JavaType abstractPointcutAdvisorType = mapper.getTypeFactory().constructType("org.springframework.aop.support.AbstractPointcutAdvisor");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), abstractPointcutAdvisorType));

        JavaType abstractBeanFactoryPointcutAdvisorType = mapper.getTypeFactory().constructType("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), abstractBeanFactoryPointcutAdvisorType));

        // Test with other Spring classes
        JavaType basicDataSourceType = mapper.getTypeFactory().constructType("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), basicDataSourceType));

        JavaType jdbcRowSetImplType = mapper.getTypeFactory().constructType("com.sun.rowset.JdbcRowSetImpl");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), jdbcRowSetImplType));
    }

    @Test
    public void testValidateSubTypeWithOtherNastyClasses() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StringDeserializer());
        mapper.registerModule(module);

        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with other nasty classes
        JavaType commonsCollectionsInvokerTransformerType = mapper.getTypeFactory().constructType("org.apache.commons.collections.functors.InvokerTransformer");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), commonsCollectionsInvokerTransformerType));

        JavaType commonsCollectionsInstantiateTransformerType = mapper.getTypeFactory().constructType("org.apache.commons.collections4.functors.InstantiateTransformer");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), commonsCollectionsInstantiateTransformerType));

        JavaType groovyConvertedClosureType = mapper.getTypeFactory().constructType("org.codehaus.groovy.runtime.ConvertedClosure");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), groovyConvertedClosureType));

        JavaType groovyMethodClosureType = mapper.getTypeFactory().constructType("org.codehaus.groovy.runtime.MethodClosure");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), groovyMethodClosureType));

        JavaType comSunOrgApacheXalanInternalXsltcTractTemplatesImplType = mapper.getTypeFactory().constructType("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), comSunOrgApacheXalanInternalXsltcTractTemplatesImplType));

        JavaType apacheXalanXsltcTractTemplatesImplType = mapper.getTypeFactory().constructType("org.apache.xalan.xsltc.trax.TemplatesImpl");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), apacheXalanXsltcTractTemplatesImplType));

        JavaType comSunRowsetJdbcRowSetImplType = mapper.getTypeFactory().constructType("com.sun.rowset.JdbcRowSetImpl");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), comSunRowsetJdbcRowSetImplType));

        JavaType javaUtilLoggingFileHandlerType = mapper.getTypeFactory().constructType("java.util.logging.FileHandler");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), javaUtilLoggingFileHandlerType));

        JavaType javaRmiServerUnicastRemoteObjectType = mapper.getTypeFactory().constructType("java.rmi.server.UnicastRemoteObject");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), javaRmiServerUnicastRemoteObjectType));

        JavaType comSunOrgApacheBcelInternalUtilClassLoaderType = mapper.getTypeFactory().constructType("com.sun.org.apache.bcel.internal.util.ClassLoader");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), comSunOrgApacheBcelInternalUtilClassLoaderType));

        JavaType apacheTomcatDbcpDbcp2BasicDataSourceType = mapper.getTypeFactory().constructType("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), apacheTomcatDbcpDbcp2BasicDataSourceType));

        JavaType apacheTomcatDbcpComboPooledDataSourceType = mapper.getTypeFactory().constructType("com.mchange.v2.c3p0.ComboPooledDataSource");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), apacheTomcatDbcpComboPooledDataSourceType));

        JavaType apacheTomcatDbcpdebugAfterCloseLoggingComboPooledDataSourceType = mapper.getTypeFactory().constructType("com.mchange.v2.c3p0.debug.AfterCloseLoggingComboPooledDataSource");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), apacheTomcatDbcpdebugAfterCloseLoggingComboPooledDataSourceType));
    }

    @Test
    public void testValidateSubTypeWithNullType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with null type
        assertThrows(NullPointerException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), null));
    }

    @Test
    public void testValidateSubTypeWithInvalidClassCastException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with invalid class cast exception
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        assertThrows(ClassCastException.class, () -> validator.validateSubType((DeserializationContext) new Object(), stringType));
    }

    @Test
    public void testValidateSubTypeWithInvalidClassNotFoundException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with invalid class not found exception
        JavaType invalidType = mapper.getTypeFactory().constructType("com.invalid.InvalidClass");
        assertThrows(ClassNotFoundException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), invalidType));
    }

    @Test
    public void testValidateSubTypeWithInvalidInvocationTargetException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with invalid invocation target exception
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StringDeserializer() {
            @Override
            public Object deserialize(DeserializationContext ctxt, BeanProperty property, String value) throws IOException, JsonProcessingException {
                throw new InvocationTargetException(new RuntimeException("Test"));
            }
        });
        mapper.registerModule(module);

        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        assertThrows(InvocationTargetException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), stringType));
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        protected StringDeserializer() {
            this(null);
        }

        protected StringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(DeserializationContext ctxt, BeanProperty property, String value) throws IOException, JsonProcessingException {
            return value;
        }
    }
}