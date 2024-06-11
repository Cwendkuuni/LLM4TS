package geo.google.mapping;

import static org.junit.Assert.*;
import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;
import org.junit.*;

public class AddressToUsAddressFunctorTest {

    // Test execute method with null address
    @Test
    public void testExecute_NullAddress_ReturnsNull() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(null);
        assertNull(result);
    }
    
    // Test execute method with address containing empty country names
    @Test
    public void testExecute_AddressWithEmptyCountryNames_ReturnsEmptyCountryInUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        country.setCountryNameCode(new ArrayList<>());
        address.getAddressDetails().setCountry(country);
        
        GeoUsAddress result = functor.execute(address);
        assertEquals("", result.getCountry());
    }
    
    // Test execute method with address containing country with country names
    @Test
    public void testExecute_AddressWithCountryNames_ReturnsCountryInUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        countryNames.add(countryNameCode);
        country.setCountryNameCode(countryNames);
        address.getAddressDetails().setCountry(country);
        
        GeoUsAddress result = functor.execute(address);
        assertEquals("US", result.getCountry());
    }
    
    // Test execute method with address containing administrative area with names
    @Test
    public void testExecute_AddressWithAdminAreaNames_ReturnsStateInUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminAreaNames.add(adminAreaName);
        adminArea.setAdministrativeAreaName(adminAreaNames);
        country.setAdministrativeArea(adminArea);
        address.getAddressDetails().setCountry(country);
        
        GeoUsAddress result = functor.execute(address);
        assertEquals("California", result.getState());
    }
    
    // Test execute method with address containing sub administrative area with names
    @Test
    public void testExecute_AddressWithSubAdminAreaNames_ReturnsCountyInUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subAdminNames = new ArrayList<>();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAdminName.setContent("Los Angeles County");
        subAdminNames.add(subAdminName);
        subAdminArea.setSubAdministrativeAreaName(subAdminNames);
        adminArea.setSubAdministrativeArea(subAdminArea);
        country.setAdministrativeArea(adminArea);
        address.getAddressDetails().setCountry(country);
        
        GeoUsAddress result = functor.execute(address);
        assertEquals("Los Angeles County", result.getCounty());
    }
    
    // Test execute method with address containing locality with names
    @Test
    public void testExecute_AddressWithLocalityNames_ReturnsCityInUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        List<Locality.LocalityName> localityNames = new ArrayList<>();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("San Francisco");
        localityNames.add(localityName);
        locality.setLocalityName(localityNames);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        address.getAddressDetails().setCountry(country);
        
        GeoUsAddress result = functor.execute(address);
        assertEquals("San Francisco", result.getCity());
    }
    
    // Test execute method with address containing postal code with numbers
    @Test
    public void testExecute_AddressWithPostalCodeNumbers_ReturnsPostalCodeInUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        PostalCode postalCode = new PostalCode();
        List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("94111");
        postalCodeNumbers.add(postalCodeNumber);
        postalCode.setPostalCodeNumber(postalCodeNumbers);
        locality.setPostalCode(postalCode);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        address.getAddressDetails().setCountry(country);
        
        GeoUsAddress result = functor.execute(address);
        assertEquals("94111", result.getPostalCode());
    }
    
    // Test execute method with address containing empty thoroughfare
    @Test
    public void testExecute_AddressWithEmptyThoroughfare_ReturnsAddressLine1EmptyInUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        address.getAddressDetails().setCountry(country);
        
        GeoUsAddress result = functor.execute(address);
        assertEquals("", result.getAddressLine1());
    }
    
    // Test execute method with address containing thoroughfare with single name
    @Test
    public void testExecute_AddressWithThoroughfareSingleName_ReturnsAddressLine1InUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        Thoroughfare thoroughfare = new Thoroughfare();
        List<ThoroughfareNameType> thoroughfareNames = new ArrayList<>();
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("123 Main St");
        thoroughfareNames.add(thoroughfareName);
        thoroughfare.setThoroughfareName(thoroughfareNames);
        locality.setThoroughfare(thoroughfare);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        address.getAddressDetails().setCountry(country);
        
        GeoUsAddress result = functor.execute(address);
        assertEquals("123 Main St", result.getAddressLine1());
    }
    
    // Test execute method with address containing thoroughfare with multiple names
    @Test
    public void testExecute_AddressWithThoroughfareMultipleNames_ReturnsAddressLine1AndAddressLine2InUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        Thoroughfare thoroughfare = new Thoroughfare();
        List<ThoroughfareNameType> thoroughfareNames = new ArrayList<>();
        ThoroughfareNameType thoroughfareName1 = new ThoroughfareNameType();
        thoroughfareName1.setContent("123 Main St");
        ThoroughfareNameType thoroughfareName2 = new ThoroughfareNameType();
        thoroughfareName2.setContent("Apt 4B");
        thoroughfareNames.add(thoroughfareName1);
        thoroughfareNames.add(thoroughfareName2);
        thoroughfare.setThoroughfareName(thoroughfareNames);
        locality.setThoroughfare(thoroughfare);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        address.getAddressDetails().setCountry(country);
        
        GeoUsAddress result = functor.execute(address);
        assertEquals("123 Main St", result.getAddressLine1());
        assertEquals("Apt 4B", result.getAddressLine2());
    }
    
    // Test execute method with address containing coordinate
    @Test
    public void testExecute_AddressWithCoordinate_ReturnsCoordinateInUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        address.setCoordinate(new Coordinate(37.7749, -122.4194));
        
        GeoUsAddress result = functor.execute(address);
        assertEquals(37.7749, result.getCoordinate().getLatitude(), 0.0001);
        assertEquals(-122.4194, result.getCoordinate().getLongitude(), 0.0001);
    }
    
    // Test execute method with address containing accuracy
    @Test
    public void testExecute_AddressWithAccuracy_ReturnsAccuracyInUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        address.setAccuracy(0.75);
        
        GeoUsAddress result = functor.execute(address);
        assertEquals(0.75, result.getAccuracy(), 0.0001);
    }
    
}