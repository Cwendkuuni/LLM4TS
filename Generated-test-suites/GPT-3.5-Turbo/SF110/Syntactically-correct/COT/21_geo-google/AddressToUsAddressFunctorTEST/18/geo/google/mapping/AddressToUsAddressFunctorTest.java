package geo.google.mapping;

import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoException;
import geo.google.datamodel.GeoUsAddress;
import oasis.names.tc.ciq.xsdschema.xal._2.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        // Test case 1: Empty address details
        GeoAddress address1 = new GeoAddress();
        GeoUsAddress result1 = functor.execute(address1);
        Assert.assertNull(result1.getCountry());
        Assert.assertNull(result1.getState());
        Assert.assertNull(result1.getCounty());
        Assert.assertNull(result1.getCity());
        Assert.assertNull(result1.getPostalCode());
        Assert.assertNull(result1.getAddressLine1());
        Assert.assertNull(result1.getAddressLine2());
        Assert.assertNull(result1.getCoordinate());
        Assert.assertEquals(0, result1.getAccuracy());

        // Test case 2: Address with only country
        GeoAddress address2 = new GeoAddress();
        AddressDetails.Country country2 = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames2 = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode2 = new AddressDetails.Country.CountryNameCode();
        countryNameCode2.setContent("US");
        countryNames2.add(countryNameCode2);
        country2.setCountryNameCode(countryNames2);
        address2.setAddressDetails(new AddressDetails());
        address2.getAddressDetails().setCountry(country2);
        GeoUsAddress result2 = functor.execute(address2);
        Assert.assertEquals("US", result2.getCountry());
        Assert.assertNull(result2.getState());
        Assert.assertNull(result2.getCounty());
        Assert.assertNull(result2.getCity());
        Assert.assertNull(result2.getPostalCode());
        Assert.assertNull(result2.getAddressLine1());
        Assert.assertNull(result2.getAddressLine2());
        Assert.assertNull(result2.getCoordinate());
        Assert.assertEquals(0, result2.getAccuracy());

        // Test case 3: Address with country and state
        GeoAddress address3 = new GeoAddress();
        AddressDetails.Country country3 = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames3 = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode3 = new AddressDetails.Country.CountryNameCode();
        countryNameCode3.setContent("US");
        countryNames3.add(countryNameCode3);
        country3.setCountryNameCode(countryNames3);
        AdministrativeArea adminArea3 = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> names3 = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName name3 = new AdministrativeArea.AdministrativeAreaName();
        name3.setContent("California");
        names3.add(name3);
        adminArea3.setAdministrativeAreaName(names3);
        country3.setAdministrativeArea(adminArea3);
        address3.setAddressDetails(new AddressDetails());
        address3.getAddressDetails().setCountry(country3);
        GeoUsAddress result3 = functor.execute(address3);
        Assert.assertEquals("US", result3.getCountry());
        Assert.assertEquals("California", result3.getState());
        Assert.assertNull(result3.getCounty());
        Assert.assertNull(result3.getCity());
        Assert.assertNull(result3.getPostalCode());
        Assert.assertNull(result3.getAddressLine1());
        Assert.assertNull(result3.getAddressLine2());
        Assert.assertNull(result3.getCoordinate());
        Assert.assertEquals(0, result3.getAccuracy());

        // Test case 4: Address with country, state, county, and city
        GeoAddress address4 = new GeoAddress();
        AddressDetails.Country country4 = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames4 = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode4 = new AddressDetails.Country.CountryNameCode();
        countryNameCode4.setContent("US");
        countryNames4.add(countryNameCode4);
        country4.setCountryNameCode(countryNames4);
        AdministrativeArea adminArea4 = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> names4 = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName name4 = new AdministrativeArea.AdministrativeAreaName();
        name4.setContent("California");
        names4.add(name4);
        adminArea4.setAdministrativeAreaName(names4);
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea subAdminArea4 = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea();
        List<AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subNames4 = new ArrayList<>();
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subName4 = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subName4.setContent("CountyName");
        subNames4.add(subName4);
        subAdminArea4.setSubAdministrativeAreaName(subNames4);
        adminArea4.setSubAdministrativeArea(subAdminArea4);
        Locality locality4 = new Locality();
        List<Locality.LocalityName> localNames4 = new ArrayList<>();
        Locality.LocalityName localName4 = new Locality.LocalityName();
        localName4.setContent("CityName");
        localNames4.add(localName4);
        locality4.setLocalityName(localNames4);
        address4.setAddressDetails(new AddressDetails());
        address4.getAddressDetails().setCountry(country4);
        address4.getAddressDetails().getCountry().setAdministrativeArea(adminArea4);
        address4.getAddressDetails().getCountry().getAdministrativeArea().setSubAdministrativeArea(subAdminArea4);
        address4.getAddressDetails().getCountry().getAdministrativeArea().setLocality(locality4);
        GeoUsAddress result4 = functor.execute(address4);
        Assert.assertEquals("US", result4.getCountry());
        Assert.assertEquals("California", result4.getState());
        Assert.assertEquals("CountyName", result4.getCounty());
        Assert.assertEquals("CityName", result4.getCity());
        Assert.assertNull(result4.getPostalCode());
        Assert.assertNull(result4.getAddressLine1());
        Assert.assertNull(result4.getAddressLine2());
        Assert.assertNull(result4.getCoordinate());
        Assert.assertEquals(0, result4.getAccuracy());

        // Test case 5: Complete address with all fields set
        GeoAddress address5 = new GeoAddress();
        AddressDetails.Country country5 = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames5 = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode5 = new AddressDetails.Country.CountryNameCode();
        countryNameCode5.setContent("US");
        countryNames5.add(countryNameCode5);
        country5.setCountryNameCode(countryNames5);
        AdministrativeArea adminArea5 = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> names5 = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName name5 = new AdministrativeArea.AdministrativeAreaName();
        name5.setContent("California");
        names5.add(name5);
        adminArea5.setAdministrativeAreaName(names5);
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea subAdminArea5 = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea();
        List<AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subNames5 = new ArrayList<>();
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subName5 = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subName5.setContent("CountyName");
        subNames5.add(subName5);
        subAdminArea5.setSubAdministrativeAreaName(subNames5);
        adminArea5.setSubAdministrativeArea(subAdminArea5);
        Locality locality5 = new Locality();
        List<Locality.LocalityName> localNames5 = new ArrayList<>();
        Locality.LocalityName localName5 = new Locality.LocalityName();
        localName5.setContent("CityName");
        localNames5.add(localName5);
        locality5.setLocalityName(localNames5);
        PostalCode postalCode5 = new PostalCode();
        List<PostalCode.PostalCodeNumber> numbers5 = new ArrayList<>();
        PostalCode.PostalCodeNumber number5 = new PostalCode.PostalCodeNumber();
        number5.setContent("12345");
        numbers5.add(number5);
        postalCode5.setPostalCodeNumber(numbers5);
        Thoroughfare thoroughfare5 = new Thoroughfare();
        List<ThoroughfareNameType> thoroughfares5 = new ArrayList<>();
        ThoroughfareNameType thoroughfareName5 = new ThoroughfareNameType();
        thoroughfareName5.setContent("Address Line 1");
        thoroughfares5.add(thoroughfareName5);
        ThoroughfareNameType thoroughfareName2 = new ThoroughfareNameType();
        thoroughfareName2.setContent("Address Line 2");
        thoroughfares5.add(thoroughfareName2);
        thoroughfare5.setThoroughfareName(thoroughfares5);
        locality5.setThoroughfare(thoroughfare5); 
        address5.setAddressDetails(new AddressDetails());
        address5.getAddressDetails().setCountry(country5);
        address5.getAddressDetails().getCountry().setAdministrativeArea(adminArea5);
        address5.getAddressDetails().getCountry().getAdministrativeArea().setSubAdministrativeArea(subAdminArea5);
        address5.getAddressDetails().getCountry().getAdministrativeArea().setLocality(locality5);
        address5.getAddressDetails().getCountry().getAdministrativeArea().getLocality().setPostalCode(postalCode5);
        GeoUsAddress result5 = functor.execute(address5);
        Assert.assertEquals("US", result5.getCountry());
        Assert.assertEquals("California", result5.getState());
        Assert.assertEquals("CountyName", result5.getCounty());
        Assert.assertEquals("CityName", result5.getCity());
        Assert.assertEquals("12345", result5.getPostalCode());
        Assert.assertEquals("Address Line 1", result5.getAddressLine1());
        Assert.assertEquals("Address Line 2", result5.getAddressLine2());
        Assert.assertNull(result5.getCoordinate());
        Assert.assertEquals(0, result5.getAccuracy());
    }
}