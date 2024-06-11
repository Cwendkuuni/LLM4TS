import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {
  
  private RecordType recordType;
  
  @Before
  public void setUp() {
    recordType = new RecordType(null, null, true);
  }
  
  @Test
  public void testIsSynthetic() {
    boolean result = recordType.isSynthetic();
    // assert statements
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(null, null, true);
    boolean tolerateUnknowns = true;
    
    boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, tolerateUnknowns);
    // assert statements
  }
  
  @Test
  public void testGetImplicitPrototype() {
    ObjectType result = recordType.getImplicitPrototype();
    // assert statements
  }
  
  @Test
  public void testDefineProperty() {
    String propertyName = "test";
    JSType type = new JSType();
    boolean inferred = false;
    Node propertyNode = new Node();
    
    boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);
    // assert statements
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = new JSType();
    
    JSType result = recordType.getGreatestSubtypeHelper(that);
    // assert statements
  }
  
  @Test
  public void testToMaybeRecordType() {
    RecordType result = recordType.toMaybeRecordType();
    // assert statements
  }
  
  @Test
  public void testIsSubtype() {
    JSType that = new JSType();
    
    boolean result = recordType.isSubtype(that);
    // assert statements
  }
  
  @Test
  public void testIsSubtype2() {
    ObjectType typeA = new ObjectType();
    RecordType typeB = new RecordType(null, null, true);
    
    boolean result = RecordType.isSubtype(typeA, typeB);
    // assert statements
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    
    JSType result = recordType.resolveInternal(t, scope);
    // assert statements
  }
  
}