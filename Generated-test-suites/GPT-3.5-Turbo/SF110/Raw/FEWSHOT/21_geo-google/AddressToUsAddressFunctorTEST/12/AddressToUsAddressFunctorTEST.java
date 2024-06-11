package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

import org.junit.Test;

public class AddressToUsAddressFunctorTest {
    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        // Test case: address without country
        GeoAddress addr1 = new GeoAddress();
        GeoUsAddress ret1 = functor.execute(addr1);
        assertNull(ret1.getCountry());
        
        // Test case: address with country but no country name code
        AddressDetails.Country country2 = new AddressDetails.Country();
        addr1.setAddressDetails(new AddressDetails());
        addr1.getAddressDetails().setCountry(country2);
        GeoUsAddress ret2 = functor.execute(addr1);
        assertNull(ret2.getCountry());
        
        // Test case: address with country and country name code
        List<AddressDetails.Country.CountryNameCode> countryNames3 = new ArrayList<AddressDetails.Country.CountryNameCode>();
        AddressDetails.Country.CountryNameCode nameCode3 = new AddressDetails.Country.CountryNameCode();
        nameCode3.setContent("US");
        countryNames3.add(nameCode3);
        country2.setCountryNameCode(countryNames3);
        GeoUsAddress ret3 = functor.execute(addr1);
        assertEquals("US", ret3.getCountry());
        
        // TODO: Add more comprehensive test cases for all paths in the method execute()
    }
}