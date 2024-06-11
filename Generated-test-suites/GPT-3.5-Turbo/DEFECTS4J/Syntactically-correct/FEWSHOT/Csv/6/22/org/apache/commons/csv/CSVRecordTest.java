
/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements. See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.apache.commons.csv;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        Assert.assertEquals("value1", record.get(TestEnum.VALUE1));
        Assert.assertEquals("value2", record.get(TestEnum.VALUE2));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByNameWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        Assert.assertEquals("value1", record.get("value1"));
        Assert.assertEquals("value2", record.get("value2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        record.get("value1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        record.get("value3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 0);
        record.get("value2");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[0], null, "comment", 0);
        Assert.assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 10);
        Assert.assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 0);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        CSVRecord record = new CSVRecord(new String[0], mapping, null, 0);
        Assert.assertTrue(record.isMapped("value1"));
        Assert.assertTrue(record.isMapped("value2"));
        Assert.assertFalse(record.isMapped("value3"));
    }

    @Test
    public void testIsMappedWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        Assert.assertFalse(record.isMapped("value1"));
    }

    @Test
    public void testIsSetWithMappingAndValidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        Assert.assertTrue(record.isSet("value1"));
        Assert.assertTrue(record.isSet("value2"));
    }

    @Test
    public void testIsSetWithMappingAndInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 0);
        Assert.assertFalse(record.isSet("value2"));
    }

    @Test
    public void testIsSetWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        Assert.assertFalse(record.isSet("value1"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        int count = 0;
        for (String value : record) {
            count++;
        }
        Assert.assertEquals(2, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        record.putIn(map);
        Assert.assertEquals("value1", map.get("key1"));
        Assert.assertEquals("value2", map.get("key2"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        Assert.assertEquals(2, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        Assert.assertEquals("value1", record.toList().get(0));
        Assert.assertEquals("value2", record.toList().get(1));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = new HashMap<>();
        map.put("value1", "value1");
        map.put("value2", "value2");
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        Assert.assertEquals(map, record.toMap());
    }

    @Test
    public void testValues() {
        String[] expectedValues = {"value1", "value2"};
        CSVRecord record = new CSVRecord(expectedValues, null, null, 0);
        Assert.assertArrayEquals(expectedValues, record.values());
    }

    private enum TestEnum {
        VALUE1,
        VALUE2
    }
}
