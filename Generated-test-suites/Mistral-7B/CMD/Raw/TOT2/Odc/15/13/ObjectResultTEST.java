package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ObjectResult extends ObjectStatus {

    /**
     * for export only, internal usage
     */
    @JsonIgnore
    private List<URL> exportPaths;

    public ObjectResult(String schema, String name, String type) {
        super(name, new AtomicLong(0), new AtomicLong(0), null);
        setType(type);
        setSchema(schema);
    }

    public static ObjectResult of(ObjectStatus that) {
        ObjectResult result = new ObjectResult();
        result.setType(that.getType());
        result.setStatus(Status.SUCCESS);
        result.setName(that.getName());
        result.setSchema(that.getSchema());
        result.setCount(new AtomicLong(that.getCount().get()));
        result.setTotal(new AtomicLong(that.getTotal().get()));
        return result;
    }

    @JsonIgnore
    public String getSummary() {
        return String.format("%s.%s[%s]", getSchema(), getName(), getType());
    }

    @Test
    public void testObjectResultConstructor() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";
        ObjectResult objectResult = new ObjectResult(schema, name, type);
        // Check if the constructor sets the correct values
        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("test_name");
        objectStatus.setType("test_type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        // Check if the method sets the correct values
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getType(), objectResult.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultEmptyConstructor() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getName());
        assertNull(objectResult.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorNullSchema() {
        String name = "test_name";
        String type = "test_type";
        ObjectResult objectResult = new ObjectResult(null, name, type);
        assertNull(objectResult.getSchema());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorNullName() {
        String schema = "test_schema";
        String type = "test_type";
        ObjectResult objectResult = new ObjectResult(schema, null, type);
        assertNull(objectResult.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultGetSummaryNullName() {
        String schema = "test_schema";
        ObjectResult objectResult = new ObjectResult(schema, null, "test_type");
        assertNull(objectResult.getSummary());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultGetSummaryInvalidSchema() {
        String name = "test_name";
        ObjectResult objectResult = new ObjectResult("test_schema%", name, "test_type");
        assertNull(objectResult.getSummary());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultGetSummaryInvalidType() {
        String schema = "test_schema";
        String name = "test_name";
        ObjectResult objectResult = new ObjectResult(schema, name, "test_type%");
        assertNull(objectResult.getSummary());
    }
}