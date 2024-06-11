
package geo.google.mapping;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class AddressToUsAddressFunctorTest {
  
  @Test
  public void testExecute() {
    AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
    GeoAddress addr = new GeoAddress();
    AddressDetails addressDetails = new AddressDetails();
    addr.setAddressDetails(addressDetails);
    
    GeoUsAddress result = functor.execute(addr);
    assertNotNull(result);
    
  }
  
}
