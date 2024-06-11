import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails;
import oasis.names.tc.ciq.xsdschema.xal._2.AdministrativeArea;
import oasis.names.tc.ciq.xsdschema.xal._2.Locality;
import oasis.names.tc.ciq.xsdschema.xal._2.PostalCode;
import oasis.names.tc.ciq.xsdschema.xal._2.ThoroughfareNameType;
import java.util.Arrays;
import java.util.List;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testExecute_returnsGeoUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress inputAddress = new GeoAddress();
        GeoUsAddress expected = new GeoUsAddress();
        GeoUsAddress result = functor.execute(inputAddress);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testExecute_setsCountry() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryCode.setContent("USA");
        country.getCountryNameCode().add(countryCode);
        inputAddress.getAddressDetails().setCountry(country);
        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setCountry("USA");
        GeoUsAddress result = functor.execute(inputAddress);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testExecute_setsState() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        inputAddress.getAddressDetails().setCountry(country);
        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setState("California");
        GeoUsAddress result = functor.execute(inputAddress);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testExecute_setsCounty_ifSubAdministrativeAreaIsNotNull() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = 
            new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAdminAreaName.setContent("County1");
        subAdminArea.getSubAdministrativeAreaName().add(subAdminAreaName);
        adminArea.setSubAdministrativeArea(subAdminArea);
        country.setAdministrativeArea(adminArea);
        inputAddress.getAddressDetails().setCountry(country);
        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setCounty("County1");
        GeoUsAddress result = functor.execute(inputAddress);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testExecute_setsCity() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("City1");
        locality.getLocalityName().add(localityName);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        inputAddress.getAddressDetails().setCountry(country);
        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setCity("City1");
        GeoUsAddress result = functor.execute(inputAddress);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testExecute_setsPostalCode() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("12345");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        locality.setPostalCode(postalCode);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        inputAddress.getAddressDetails().setCountry(country);
        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setPostalCode("12345");
        GeoUsAddress result = functor.execute(inputAddress);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testExecute_setsAddressLine1_andAddressLine2_ifThoroughfaresHasMoreThanOne() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        ThoroughfareNameType thoroughfare1 = new ThoroughfareNameType();
        thoroughfare1.setContent("Address Line 1");
        ThoroughfareNameType thoroughfare2 = new ThoroughfareNameType();
        thoroughfare2.setContent("Address Line 2");
        locality.getThoroughfare().getThoroughfareName().addAll(Arrays.asList(thoroughfare1, thoroughfare2));
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        inputAddress.getAddressDetails().setCountry(country);
        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setAddressLine1("Address Line 1");
        expectedResult.setAddressLine2("Address Line 2");
        GeoUsAddress result = functor.execute(inputAddress);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testExecute_setsCoordinate() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress inputAddress = new GeoAddress();
        GeoCoordinate coordinate = new GeoCoordinate();
        inputAddress.setCoordinate(coordinate);
        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setCoordinate(coordinate.clone());
        GeoUsAddress result = functor.execute(inputAddress);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testExecute_setsAccuracy() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress inputAddress = new GeoAddress();
        inputAddress.setAccuracy(0.5);
        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setAccuracy(0.5);
        GeoUsAddress result = functor.execute(inputAddress);
        
        assertEquals(expectedResult, result);
    }
}