package geo.google.mapping;

import static org.junit.Assert.assertEquals;
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
        // Test data setup
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        addr.setAddressDetails(addressDetails);
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        country.setCountryNameCode(countryNames);
        countryNames.add(new AddressDetails.Country.CountryNameCode("US"));
        
        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);
        List<AdministrativeArea.AdministrativeAreaName> names = new ArrayList<>();
        adminArea.setAdministrativeAreaName(names);
        names.add(new AdministrativeArea.AdministrativeAreaName("State"));
        
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        adminArea.setSubAdministrativeArea(subAdminArea);
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subNames = new ArrayList<>();
        subAdminArea.setSubAdministrativeAreaName(subNames);
        subNames.add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName("County"));
        
        Locality locality = new Locality();
        subAdminArea.setLocality(locality);
        List<Locality.LocalityName> localNames = new ArrayList<>();
        locality.setLocalityName(localNames);
        localNames.add(new Locality.LocalityName("City"));
        
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        List<PostalCode.PostalCodeNumber> numbers = new ArrayList<>();
        postalCode.setPostalCodeNumber(numbers);
        numbers.add(new PostalCode.PostalCodeNumber("12345"));
        
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
        thoroughfare.setThoroughfareName(thoroughfares);
        thoroughfares.add(new ThoroughfareNameType("Address Line 1"));
        thoroughfares.add(new ThoroughfareNameType("Address Line 2"));
        
        // Expected result
        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setCountry("US");
        expectedResult.setState("State");
        expectedResult.setCounty("County");
        expectedResult.setCity("City");
        expectedResult.setPostalCode("12345");
        expectedResult.setAddressLine1("Address Line 1");
        expectedResult.setAddressLine2("Address Line 2");

        // Test execution
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(addr);
        
        // Verify result
        assertEquals(expectedResult.getCountry(), result.getCountry());
        assertEquals(expectedResult.getState(), result.getState());
        assertEquals(expectedResult.getCounty(), result.getCounty());
        assertEquals(expectedResult.getCity(), result.getCity());
        assertEquals(expectedResult.getPostalCode(), result.getPostalCode());
        assertEquals(expectedResult.getAddressLine1(), result.getAddressLine1());
        assertEquals(expectedResult.getAddressLine2(), result.getAddressLine2());
    }
}