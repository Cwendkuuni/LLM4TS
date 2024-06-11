import static org.junit.Assert.*;
import org.junit.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testGetInstance() {
        AddressToUsAddressFunctor instance = AddressToUsAddressFunctor.getInstance();
        assertNotNull(instance);
    }
    
    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        // Test null address
        GeoUsAddress result = functor.execute(null);
        assertNull(result);
        
        // Create a sample GeoAddress
        GeoAddress addr = new GeoAddress();
        AddressDetails details = new AddressDetails();
        addr.setAddressDetails(details);
        
        // Test empty address
        result = functor.execute(addr);
        assertNotNull(result);
        assertNull(result.getCountry());
        
        // Test non-null country
        details.setCountry(new AddressDetails.Country());
        result = functor.execute(addr);
        assertNotNull(result);
        assertNull(result.getCountry());
        
        // Test country with names
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode nameCode = new AddressDetails.Country.CountryNameCode();
        nameCode.setContent("US");
        countryNames.add(nameCode);
        details.getCountry().setCountryNameCode(countryNames);
        result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        
        // Test administrative area
        AddressDetails.Country.AdministrativeArea adminArea = new AddressDetails.Country.AdministrativeArea();
        details.getCountry().setAdministrativeArea(adminArea);
        result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertNull(result.getState());
        
        // Test state
        List<AddressDetails.Country.AdministrativeArea.AdministrativeAreaName> stateNames = new ArrayList<>();
        AddressDetails.Country.AdministrativeArea.AdministrativeAreaName stateName = new AddressDetails.Country.AdministrativeArea.AdministrativeAreaName();
        stateName.setContent("California");
        stateNames.add(stateName);
        adminArea.setAdministrativeAreaName(stateNames);
        result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        
        // Test sub administrative area
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea subAdminArea = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea();
        adminArea.setSubAdministrativeArea(subAdminArea);
        result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertNull(result.getCounty());
        
        // Test county
        List<AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> countyNames = new ArrayList<>();
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName countyName = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        countyName.setContent("Los Angeles");
        countyNames.add(countyName);
        subAdminArea.setSubAdministrativeAreaName(countyNames);
        result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        
        // Test locality
        subAdminArea.setLocality(new Locality());
        result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        
        // Test city
        List<Locality.LocalityName> cityNames = new ArrayList<>();
        Locality.LocalityName cityName = new Locality.LocalityName();
        cityName.setContent("Los Angeles City");
        cityNames.add(cityName);
        subAdminArea.getLocality().setLocalityName(cityNames);
        result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles City", result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        
        // Test postal code
        subAdminArea.getLocality().setPostalCode(new PostalCode());
        List<PostalCode.PostalCodeNumber> numbers = new ArrayList<>();
        PostalCode.PostalCodeNumber number = new PostalCode.PostalCodeNumber();
        number.setContent("90001");
        numbers.add(number);
        subAdminArea.getLocality().getPostalCode().setPostalCodeNumber(numbers);
        result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles City", result.getCity());
        assertEquals("90001", result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        
        // Test thoroughfare
        subAdminArea.getLocality().setThoroughfare(new Locality.Thoroughfare());
        List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
        ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
        thoroughfare.setContent("Main St");
        thoroughfares.add(thoroughfare);
        subAdminArea.getLocality().getThoroughfare().setThoroughfareName(thoroughfares);
        result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles City", result.getCity());
        assertEquals("90001", result.getPostalCode());
        assertEquals("Main St", result.getAddressLine1());
        assertNull(result.getAddressLine2());
        
        // Test additional address line
        ThoroughfareNameType thoroughfare2 = new ThoroughfareNameType();
        thoroughfare2.setContent("Second St");
        thoroughfares.add(thoroughfare2);
        result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles City", result.getCity());
        assertEquals("90001", result.getPostalCode());
        assertEquals("Main St", result.getAddressLine1());
        assertEquals("Second St", result.getAddressLine2());
        
        // Test coordinate and accuracy
        addr.setCoordinate(new GeoCoordinate());
        result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Los Angeles City", result.getCity());
        assertEquals("90001", result.getPostalCode());
        assertEquals("Main St", result.getAddressLine1());
        assertEquals("Second St", result.getAddressLine2());
        assertNotNull(result.getCoordinate());
        assertEquals(0.0, result.getCoordinate().getLatitude(), 0.0001);
        assertEquals(0.0, result.getCoordinate().getLongitude(), 0.0001);
        assertNull(result.getAccuracy());
    }
}

