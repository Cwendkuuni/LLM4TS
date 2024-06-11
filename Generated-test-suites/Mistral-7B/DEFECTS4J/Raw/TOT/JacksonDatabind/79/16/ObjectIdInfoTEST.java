package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.PropertyName;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructionWithValidArguments() {
        PropertyName propName = new PropertyName("idProp");
        Class<String> scope = String.class;
        Class<IdGenerator> idGenerator = JsonIdentityInfo.IdGenerator.Name.ID_PROPERTY.getClass();

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, idGenerator);

        assertNotNull(objectIdInfo);
        assertEquals(propName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(idGenerator, objectIdInfo.getGeneratorType());
        assertNotNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithFourValidArguments() {
        PropertyName propName = new PropertyName("idProp");
        Class<String> scope = String.class;
        Class<IdGenerator> idGenerator = JsonIdentityInfo.IdGenerator.Name.ID_PROPERTY.getClass();
        boolean alwaysAsId = false;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, idGenerator, alwaysAsId);

        assertNotNull(objectIdInfo);
        assertEquals(propName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(idGenerator, objectIdInfo.getGeneratorType());
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
        assertNotNull(objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfoConstructionWithValidArgumentsAndWithAlwaysAsId() {
        PropertyName propName = new PropertyName("idProp");
        Class<String> scope = String.class;
        Class<IdGenerator> idGenerator = JsonIdentityInfo.IdGenerator.Name.ID_PROPERTY.getClass();
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, idGenerator);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(alwaysAsId);

        assertNotNull(objectIdInfo);
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertEquals(objectIdInfo, objectIdInfoWithAlwaysAsId);
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }
}