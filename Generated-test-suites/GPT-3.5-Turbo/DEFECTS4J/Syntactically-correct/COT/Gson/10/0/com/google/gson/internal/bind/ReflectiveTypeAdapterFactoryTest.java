import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testExcludeField() {
    ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory();
    Field field = new Field();
    
    assertTrue(adapterFactory.excludeField(field, true));
    assertTrue(adapterFactory.excludeField(field, false));
  }
  
  @Test
  public void testGetFieldNames() {
    ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory();
    Field field = new Field();
    
    List<String> fieldNames = adapterFactory.getFieldNames(field);
    assertEquals(Collections.singletonList(field.getName()), fieldNames);
    
    field.addAnnotation(new SerializedName("name"));
    field.addAlternate("alt1");
    field.addAlternate("alt2");
    
    fieldNames = adapterFactory.getFieldNames(field);
    assertEquals(Arrays.asList("name", "alt1", "alt2"), fieldNames);
  }
  
  @Test
  public void testCreate() {
    ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory();
    Gson gson = new Gson();
    TypeToken<String> typeToken = new TypeToken<String>();
    
    TypeAdapter<String> typeAdapter = adapterFactory.create(gson, typeToken);
    assertNotNull(typeAdapter);
  }
  
  @Test
  public void testCreate_PrimitiveType() {
    ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory();
    Gson gson = new Gson();
    TypeToken<Integer> typeToken = new TypeToken<Integer>();
    
    TypeAdapter<Integer> typeAdapter = adapterFactory.create(gson, typeToken);
    assertNull(typeAdapter);
  }
  
  @Test(expected=IllegalArgumentException)
  public void testCreate_MultipleFieldsWithSameName() {
    ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory();
    Gson gson = new Gson();
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>();

    adapterFactory.create(gson, typeToken);
  }
  
  static class Field {
    private String name;
    private List<String> alternates;
    
    public void addAnnotation(SerializedName annotation) {
      name = annotation.value();
      alternates = new ArrayList<String>(Arrays.asList(annotation.alternate()));
    }
    
    public void addAlternate(String alternate) {
      alternates.add(alternate);
    }
    
    public String getName() {
      return name;
    }
    
    public String[] getAlternates() {
      return alternates.toArray(new String[alternates.size()]);
    }
  }
  
  static class SerializedName {
    private final String value;
    private final String[] alternate;
    
    public SerializedName(String value) {
      this(value, new String[]{});
    }
    
    public SerializedName(String value, String[] alternate) {
      this.value = value;
      this.alternate = alternate;
    }
    
    public String value() {
      return value;
    }
    
    public String[] alternate() {
      return alternate;
    }
  }
  
  static class TestClass {
    @SerializedName("name")
    private String firstName;
    
    @SerializedName("name")
    private String lastName;
  }
}