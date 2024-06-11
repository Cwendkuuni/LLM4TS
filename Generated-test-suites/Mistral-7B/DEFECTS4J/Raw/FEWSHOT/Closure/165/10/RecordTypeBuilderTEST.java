package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import java.util.Collections;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType result = builder.build();
    JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void testAddProperty() {
    String propertyName = "name";
    JSType propertyType = JSType.STRING;
    Node propertyNode = mock(Node.class);

    builder.addProperty(propertyName, propertyType, propertyNode);

    RecordTypeBuilder expectedBuilder = new RecordTypeBuilder(registry);
    expectedBuilder.addProperty(propertyName, propertyType, propertyNode);

    RecordType result = (RecordType) builder.build();
    RecordType expectedRecordType = new RecordType(registry,
        Maps.newHashMap(Collections.singletonMap(propertyName,
            new RecordProperty(propertyType, propertyNode))));

    assertThat(result).isEqualTo(expectedRecordType);
  }

  @Test
  public void testAddDuplicateProperty() {
    String propertyName = "name";
    JSType propertyType = JSType.STRING;
    Node propertyNode = mock(Node.class);

    builder.addProperty(propertyName, propertyType, propertyNode);

    assertNull(builder.addProperty(propertyName, propertyType, propertyNode));
  }

  @Test
  public void testBuildRecordWithProperties() {
    String propertyName1 = "name1";
    String propertyName2 = "name2";
    JSType propertyType1 = JSType.STRING;
    JSType propertyType2 = JSType.NUMBER;
    Node propertyNode1 = mock(Node.class);
    Node propertyNode2 = mock(Node.class);

    builder.addProperty(propertyName1, propertyType1, propertyNode1);
    builder.addProperty(propertyName2, propertyType2, propertyNode2);

    RecordType result = (RecordType) builder.build();
    HashMap<String, RecordProperty> expectedProperties = Maps.newHashMap();
    expectedProperties.put(propertyName1,
        new RecordProperty(propertyType1, propertyNode1));
    expectedProperties.put(propertyName2,
        new RecordProperty(propertyType2, propertyNode2));

    RecordType expectedRecordType = new RecordType(registry,
        Collections.unmodifiableMap(expectedProperties));

    assertThat(result).isEqualTo(expectedRecordType);
  }
}