import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.mapping.AddressToUsAddressFunctor;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;
import geo.google.datamodel.*;
import org.apache.commons.collections.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        // Create a dummy GeoAddress object
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        GeoUsAddress expected = new GeoUsAddress();
        
        // Set values for the dummy addressDetails object
        AddressDetails.Country country = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        countryNames.add(countryNameCode);
        country.setCountryNameCode(countryNames);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> adminNames = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName adminName = new AdministrativeArea.AdministrativeAreaName();
        adminName.setContent("California");
        adminNames.add(adminName);
        adminArea.setAdministrativeAreaName(adminNames);
        
        Locality locality = new Locality();
        List<Locality.LocalityName> localNames = new ArrayList<>();
        Locality.LocalityName localName = new Locality.LocalityName();
        localName.setContent("San Francisco");
        localNames.add(localName);
        locality.setLocalityName(localNames);
        
        PostalCode postalCode = new PostalCode();
        List<PostalCode.PostalCodeNumber> numbers = new ArrayList<>();
        PostalCode.PostalCodeNumber number = new PostalCode.PostalCodeNumber();
        number.setContent("94111");
        numbers.add(number);
        postalCode.setPostalCodeNumber(numbers);
        locality.setPostalCode(postalCode);
        
        Thoroughfare thoroughfare = new Thoroughfare();
        List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("Street A");
        thoroughfares.add(thoroughfareName);
        thoroughfare.setThoroughfareName(thoroughfares);
        locality.setThoroughfare(thoroughfare);
        
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        country.setCountryNameCode(countryNames);
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);
        
        // Set expected values for the dummy GeoUsAddress object
        expected.setCountry("USA");
        expected.setState("California");
        expected.setCity("San Francisco");
        expected.setPostalCode("94111");
        expected.setAddressLine1("Street A");
        
        // Test the execute method
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(addr);
        
        // Check if the result matches the expected GeoUsAddress object
        assertNotNull(result);
        assertEquals(expected.getCountry(), result.getCountry());
        assertEquals(expected.getState(), result.getState());
        assertEquals(expected.getCity(), result.getCity());
        assertEquals(expected.getPostalCode(), result.getPostalCode());
        assertEquals(expected.getAddressLine1(), result.getAddressLine1());
    }
}