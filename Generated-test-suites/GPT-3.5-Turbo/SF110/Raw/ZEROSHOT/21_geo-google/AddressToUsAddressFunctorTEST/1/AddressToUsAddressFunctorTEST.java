
package geo.google.mapping;

import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;
import geo.google.GeoException;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails;
import oasis.names.tc.ciq.xsdschema.xal._2.AdministrativeArea;
import oasis.names.tc.ciq.xsdschema.xal._2.Locality;
import oasis.names.tc.ciq.xsdschema.xal._2.PostalCode;
import oasis.names.tc.ciq.xsdschema.xal._2.ThoroughfareNameType;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress geoAddress = new GeoAddress();
        // Initialize geoAddress
        
        GeoUsAddress expected = new GeoUsAddress();
        // Initialize expected
        
        GeoUsAddress actual = functor.execute(geoAddress);
        
        // Assert statements for comparing expected and actual
        
        Assert.assertEquals(expected, actual);
    }
}
