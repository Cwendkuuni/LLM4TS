package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testExecute() {
    
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        geoAddress.setAddressDetails(addressDetails);
        
        // Test case when country is not null
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);
        
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        countryNames.add(new AddressDetails.Country.CountryNameCode());
        countryNames.get(0).setContent("US");
        country.setCountryNameCode(countryNames);
        
        AdministrativeArea administrativeArea = new AdministrativeArea();
        country.setAdministrativeArea(administrativeArea);
        
        List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = new ArrayList<>();
        adminAreaNames.add(new AdministrativeArea.AdministrativeAreaName());
        adminAreaNames.get(0).setContent("California");
        administrativeArea.setAdministrativeAreaName(adminAreaNames);
        
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        administrativeArea.setSubAdministrativeArea(subAdminArea);
        
        Locality locality = new Locality();
        subAdminArea.setLocality(locality);
        
        List<Locality.LocalityName> localNames = new ArrayList<>();
        localNames.add(new Locality.LocalityName());
        localNames.get(0).setContent("Los Angeles");
        locality.setLocalityName(localNames);
        
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        
        List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
        postalCodeNumbers.add(new PostalCode.PostalCodeNumber());
        postalCodeNumbers.get(0).setContent("90001");
        postalCode.setPostalCodeNumber(postalCodeNumbers);
        
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        
        List<ThoroughfareNameType> thoroughfareNames = new ArrayList<>();
        thoroughfareNames.add(new ThoroughfareNameType());
        thoroughfareNames.get(0).setContent("Main Street");
        thoroughfare.setThoroughfareName(thoroughfareNames);
        
        GeoUsAddress expected = new GeoUsAddress();
        expected.setCountry("US");
        expected.setState("California");
        expected.setCounty("");
        expected.setCity("Los Angeles");
        expected.setPostalCode("90001");
        expected.setAddressLine1("Main Street");
        expected.setAddressLine2("");
        
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress actual = functor.execute(geoAddress);
        
        assertNotNull(actual);
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getCounty(), actual.getCounty());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getPostalCode(), actual.getPostalCode());
        assertEquals(expected.getAddressLine1(), actual.getAddressLine1());
        assertEquals(expected.getAddressLine2(), actual.getAddressLine2());
    }
}