import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import geo.google.datamodel.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {
    
    private AddressToUsAddressFunctor functor;
    private GeoUsAddress expected;
    
    @Before
    public void setUp() {
        functor = AddressToUsAddressFunctor.getInstance();
        expected = new GeoUsAddress();
    }
    
    @Test
    public void testExecute_WithNullAddress_ReturnsNull() {
        GeoAddress addr = null;
        GeoUsAddress result = functor.execute(addr);
        assertNull(result);
    }
    
    @Test
    public void testExecute_WithCountryAndState_ReturnsExpectedUsAddress() {
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryCode.setContent("US");
        country.getCountryNameCode().add(countryCode);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        expected.setCountry("US");
        expected.setState("California");
        
        GeoUsAddress result = functor.execute(addr);
        assertEquals(expected, result);
    }
    
    @Test
    public void testExecute_WithCountryAndStateAndCounty_ReturnsExpectedUsAddress() {
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryCode.setContent("US");
        country.getCountryNameCode().add(countryCode);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAdminAreaName.setContent("County Name");
        subAdminArea.getSubAdministrativeAreaName().add(subAdminAreaName);
        adminArea.setSubAdministrativeArea(subAdminArea);
        
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        expected.setCountry("US");
        expected.setState("California");
        expected.setCounty("County Name");
        
        GeoUsAddress result = functor.execute(addr);
        assertEquals(expected, result);
    }
    
    @Test
    public void testExecute_WithCountryAndStateAndLocality_ReturnsExpectedUsAddress() {
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryCode.setContent("US");
        country.getCountryNameCode().add(countryCode);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("City Name");
        locality.getLocalityName().add(localityName);
        adminArea.setLocality(locality);
        
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        expected.setCountry("US");
        expected.setState("California");
        expected.setCity("City Name");
        
        GeoUsAddress result = functor.execute(addr);
        assertEquals(expected, result);
    }
    
    @Test
    public void testExecute_WithCountryAndStateAndLocalityAndPostalCode_ReturnsExpectedUsAddress() {
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryCode.setContent("US");
        country.getCountryNameCode().add(countryCode);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("City Name");
        locality.getLocalityName().add(localityName);
        adminArea.setLocality(locality);
        
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("12345");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        locality.setPostalCode(postalCode);
        
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        expected.setCountry("US");
        expected.setState("California");
        expected.setCity("City Name");
        expected.setPostalCode("12345");
        
        GeoUsAddress result = functor.execute(addr);
        assertEquals(expected, result);
    }
    
    @Test
    public void testExecute_WithCountryAndStateAndLocalityAndThoroughfare_ReturnsExpectedUsAddress() {
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryCode.setContent("US");
        country.getCountryNameCode().add(countryCode);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("City Name");
        locality.getLocalityName().add(localityName);
        adminArea.setLocality(locality);
        
        Thoroughfare thoroughfare = new Thoroughfare();
        Thoroughfare.ThoroughfareName thoroughfareName1 = new Thoroughfare.ThoroughfareName();
        thoroughfareName1.setContent("Street 1");
        thoroughfare.getThoroughfareName().add(thoroughfareName1);
        locality.setThoroughfare(thoroughfare);
        
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        expected.setCountry("US");
        expected.setState("California");
        expected.setCity("City Name");
        expected.setAddressLine1("Street 1");
        
        GeoUsAddress result = functor.execute(addr);
        assertEquals(expected, result);
    }
    
    @Test
    public void testExecute_WithCountryAndStateAndLocalityAndThoroughfareMultiple_ReturnsExpectedUsAddress() {
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryCode.setContent("US");
        country.getCountryNameCode().add(countryCode);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("City Name");
        locality.getLocalityName().add(localityName);
        adminArea.setLocality(locality);
        
        Thoroughfare thoroughfare = new Thoroughfare();
        Thoroughfare.ThoroughfareName thoroughfareName1 = new Thoroughfare.ThoroughfareName();
        thoroughfareName1.setContent("Street 1");
        thoroughfare.getThoroughfareName().add(thoroughfareName1);
        Thoroughfare.ThoroughfareName thoroughfareName2 = new Thoroughfare.ThoroughfareName();
        thoroughfareName2.setContent("Street 2");
        thoroughfare.getThoroughfareName().add(thoroughfareName2);
        locality.setThoroughfare(thoroughfare);
        
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        expected.setCountry("US");
        expected.setState("California");
        expected.setCity("City Name");
        expected.setAddressLine1("Street 1");
        expected.setAddressLine2("Street 2");
        
        GeoUsAddress result = functor.execute(addr);
        assertEquals(expected, result);
    }
    
    @Test
    public void testExecute_WithAddressCoordinate_ReturnsExpectedUsAddress() {
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryCode.setContent("US");
        country.getCountryNameCode().add(countryCode);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        
        GeoCoordinate coordinate = new GeoCoordinate();
        coordinate.setLatitude(37.7749);
        coordinate.setLongitude(-122.4194);
        
        GeoAddress addr = new GeoAddress();
        addr.setCoordinate(coordinate);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        expected.setCountry("US");
        expected.setState("California");
        expected.setCoordinate(coordinate.clone());
        
        GeoUsAddress result = functor.execute(addr);
        assertEquals(expected, result);
    }
    
    @Test
    public void testExecute_WithAddressAccuracy_ReturnsExpectedUsAddress() {
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryCode.setContent("US");
        country.getCountryNameCode().add(countryCode);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        
        GeoAddress addr = new GeoAddress();
        addr.setAccuracy(1);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        expected.setCountry("US");
        expected.setState("California");
        expected.setAccuracy(1);
        
        GeoUsAddress result = functor.execute(addr);
        assertEquals(expected, result);
    }

}