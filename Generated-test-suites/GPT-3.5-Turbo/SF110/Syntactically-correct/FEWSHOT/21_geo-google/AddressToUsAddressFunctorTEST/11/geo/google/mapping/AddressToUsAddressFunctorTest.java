package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.AdministrativeArea;
import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;
import geo.google.datamodel.Locality;
import geo.google.datamodel.PostalCode;
import geo.google.datamodel.ThoroughfareNameType;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testExecute() {
        // Setup
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        geoAddress.setAddressDetails(addressDetails);
        GeoUsAddress expected = new GeoUsAddress();
        expected.setAccuracy("Some accuracy");
        expected.setCountry("Some country");
        expected.setState("Some state");
        expected.setCounty("Some county");
        expected.setCity("Some city");
        expected.setPostalCode("Some postal code");
        expected.setAddressLine1("Some address line 1");
        expected.setAddressLine2("Some address line 2");
        
        // Test
        GeoUsAddress actual = addressToUsAddressFunctor.execute(geoAddress);

        // Verify
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getCounty(), actual.getCounty());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getPostalCode(), actual.getPostalCode());
        assertEquals(expected.getAddressLine1(), actual.getAddressLine1());
        assertEquals(expected.getAddressLine2(), actual.getAddressLine2());
        assertEquals(expected.getAccuracy(), actual.getAccuracy());

    }
}